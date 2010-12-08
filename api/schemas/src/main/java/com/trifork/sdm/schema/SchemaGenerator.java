package com.trifork.sdm.schema;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InvalidClassException;
import java.io.OutputStreamWriter;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import org.apache.commons.io.FileUtils;
import org.reflections.Reflections;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.scanners.TypeElementsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;

import com.trifork.sdm.Documented;
import com.trifork.sdm.Versioned;


/**
 * Goal that creates schemas for annotated classes.
 */
public class SchemaGenerator {

	public static void main(String[] args) throws IOException, URISyntaxException {

		SchemaGenerator generator = new SchemaGenerator(args[0], args[1]);
		generator.execute();
	}


	/**
	 * The directory where the schemas are placed (.xsd files).
	 */
	public String target;

	/**
	 * The base package to generate models from.
	 */
	public String packageName;


	public SchemaGenerator(String destination, String packageName) {

		this.target = destination;
		this.packageName = packageName;
	}


	public void execute() throws IOException, URISyntaxException {

		Reflections reflections = new Reflections(new ConfigurationBuilder()
				.setUrls(ClasspathHelper.getUrlsForPackagePrefix(packageName))
				.filterInputsBy(new FilterBuilder().include(FilterBuilder.prefix(packageName)))
				.setScanners(new TypeAnnotationsScanner(), new TypeElementsScanner()));

		// Copy the Stamdata element type definitions to the destination.

		File typesFileDestination = new File(target, "Stamdata.xsd");

		if (!typesFileDestination.exists()) {

			File typesFile = new File(getClass().getClassLoader().getResource("Stamdata.xsd").toURI());
			FileUtils.copyFile(typesFile, typesFileDestination);
		}

		Set<Class<?>> entities = reflections.getTypesAnnotatedWith(Entity.class);

		for (Class<?> entity : entities) {

			System.out.println("Entity: " + entity.getSimpleName());

			Entity typeAnnotation = entity.getAnnotation(Entity.class);
			Versioned versioned = entity.getAnnotation(Versioned.class);

			if (versioned.value() == null || versioned.value().length == 0) {

				generateSchemaVersion(entity, typeAnnotation, 1);
			}
			else
				for (int version : versioned.value()) {

					generateSchemaVersion(entity, typeAnnotation, version);
				}
		}
	}


	private void generateSchemaVersion(Class<?> type, Entity typeAnnotation, int version) throws IOException {

		final String STAMDATA_NAMESPACE = "http://www.stamdata.dk/2010/StamdataXML";
		final String XML_SCHEMA_NAMESPACE = "http://www.w3.org/2001/XMLSchema";

		// Determine the schema's name.

		String schemaName;

		if (!typeAnnotation.name().isEmpty()) {
			schemaName = typeAnnotation.name().toLowerCase();
		}
		else {
			schemaName = type.getSimpleName().toLowerCase();
		}

		// Make sure the destination folder exists.
		// TODO: Move up to execute.

		new File(target).mkdirs();

		System.out.println(String.format("Generating Schema: %s (v%d)", schemaName, version));

		String schemaCollectionFilePath = String.format("%s/%s_collection_v%d.xsd", target, schemaName,
				version);
		OutputStreamWriter outputCollection = new OutputStreamWriter(new FileOutputStream(
				schemaCollectionFilePath), "UTF-8");
		BufferedWriter writerCollection = new BufferedWriter(outputCollection);

		write(writerCollection, 0, String.format(
				"<xs:schema elementFormDefault=\"unqualified\" xmlns:xs=\"%s\" xmlns:sd=\"%s\">",
				XML_SCHEMA_NAMESPACE, STAMDATA_NAMESPACE));
		write(writerCollection, 1, String.format(
				"<xs:import namespace=\"%s\" schemaLocation=\"Stamdata.xsd\" />", STAMDATA_NAMESPACE));

		write(writerCollection, 1,
				String.format("<xs:include schemaLocation=\"%s_v%d.xsd\" />", schemaName, version));

		write(writerCollection, 1, String.format("<xs:element name=\"%sCollection\">", schemaName));
		write(writerCollection, 1, "<xs:complexType>");
		write(writerCollection, 2, "<xs:sequence>");
		write(writerCollection, 3, String.format(
				"<xs:element name=\"%s\" type=\"%s\" maxOccurs=\"unbounded\" />", schemaName, schemaName));
		write(writerCollection, 2, "</xs:sequence>");
		write(writerCollection, 1, "</xs:complexType>");
		write(writerCollection, 1, "</xs:element>");
		write(writerCollection, 0, "</xs:schema>");

		writerCollection.close();

		String schemaFilePath = String.format("%s/%s_v%d.xsd", target, schemaName, version);
		OutputStreamWriter output = new OutputStreamWriter(new FileOutputStream(schemaFilePath), "UTF-8");
		BufferedWriter writer = new BufferedWriter(output);

		write(writer, 0, "<?xml version=\"1.0\" encoding=\"utf-8\"?>");

		// Import the Stamdata namespace.

		write(writer, 0, String.format(
				"<xs:schema elementFormDefault=\"unqualified\" xmlns:xs=\"%s\" xmlns:sd=\"%s\">",
				XML_SCHEMA_NAMESPACE, STAMDATA_NAMESPACE));
		write(writer, 1, String.format("<xs:import namespace=\"%s\" schemaLocation=\"Stamdata.xsd\" />",
				STAMDATA_NAMESPACE));

		// Begin the element itself.

		write(writer, 1, String.format("<xs:complexType name=\"%s\">", schemaName));

		// Write the documentation annotation if it exists.

		Documented documentation = type.getAnnotation(Documented.class);

		if (documentation != null && !documentation.value().isEmpty()) {

			write(writer, 2, "<xs:annotation>");
			write(writer, 3, "<xs:documentation><![CDATA[");
			write(writer, 4, documentation.value());
			write(writer, 3, "]]></xs:documentation>");
			write(writer, 2, "</xs:annotation>");
		}
		else {
			System.err.println(String.format(
					"The class '%s' is not documented! Add documentation to the @Column annotation.",
					type.getSimpleName()));
		}

		write(writer, 3, "<xs:all>");

		// We want the elements to appear in alphabetical order.

		SortedMap<String, Method> elements = findAnnotatedProperties(type, version);

		// Output each of the found element properties to the schema.

		for (Map.Entry<String, Method> entry : elements.entrySet()) {

			String name = entry.getKey();
			Method method = entry.getValue();

			String xmlType = getElementType(type, method);

			String occurString = "";

			// If the property is the ID property we set minOccurs="1".

			if (method.isAnnotationPresent(Id.class)) {
				occurString = "minOccurs=\"1\"";
			}
			else {
				occurString = "minOccurs=\"0\"";
			}

			// Create the element in the schema.

			Column annotation = method.getAnnotation(Column.class);

			// If the property has a length restriction we have to restrict the
			// content.

			if (annotation.length() == 255) {
				String element = String.format("<xs:element name=\"%s\" type=\"%s\" %s />", name, xmlType,
						occurString);
				write(writer, 4, element);
			}
			else {
				write(writer, 4, String.format("<xs:element name=\"%s\" %s>", name, occurString));
				write(writer, 5, "<xs:complexType>");
				write(writer, 6, "<xs:simpleContent>");
				write(writer, 7, String.format("<xs:restriction base=\"%s\">", xmlType));

				if (xmlType.equals("sd:restrictedString"))
					write(writer, 8, String.format("<xs:maxLength value=\"%d\"/>", annotation.length()));
				else {
					// creates char array with 'length()' elements
					// fill each element of chars array with '9'.
					char[] chars = new char[annotation.length()];
					Arrays.fill(chars, '9');
					write(writer, 8, String.format("<xs:maxInclusive value=\"%s\" />", String.valueOf(chars)));
				}

				write(writer, 7, "</xs:restriction>");
				write(writer, 6, "</xs:simpleContent>");
				write(writer, 5, "</xs:complexType>");
				write(writer, 4, "</xs:element>");
			}
		}

		// Technical Fields

		writeAuditMethod(type, "getValidFrom", writer);
		writeAuditMethod(type, "getValidTo", writer);

		write(writer, 3, "</xs:all>");

		// Technical Fields

		write(writer, 3, "<xs:attribute name=\"recordId\" type=\"xs:long\" />");
		write(writer, 3, "<xs:attribute name=\"updateToken\" type=\"xs:long\" />");

		write(writer, 1, "</xs:complexType>");
		write(writer, 0, "</xs:schema>");

		writer.close();
		output.close();
	}


	private void writeAuditMethod(Class<?> type, String methodName, BufferedWriter writer) throws IOException {

		try {
			Method method = type.getMethod(methodName);

			String elementName = getElementNameFromMethod(method, null);

			write(writer, 4, String.format("<xs:element name=\"%s\" type=\"%s\" minOccurs=\"1\" />",
					elementName, "xs:dateTime"));
		}
		catch (NoSuchMethodException e) {

			String message = String.format("ERROR: Entity '%s' did not have a method named '%s'.",
					type.getSimpleName(), methodName);

			System.out.println(message);
		}
	}


	private String getElementType(Class<?> type, Method method) throws InvalidClassException {

		// Determine the type of the element based on the property's
		// return type.

		Class<?> returnType = method.getReturnType();
		String elementType;

		// At the moment we only support Long, int, boolean, Date and string.
		// Add types here if you need them.

		if (returnType == Long.class || returnType == int.class || returnType == Integer.class
				|| returnType == long.class) {
			elementType = "sd:restrictedInteger";
		}
		else if (returnType == double.class || returnType == Double.class || returnType == float.class
				|| returnType == Float.class) {
			elementType = "sd:restrictedDouble";
		}
		else if (returnType == Date.class) {
			elementType = "sd:restrictedDateTime";
		}
		else if (returnType == String.class) {
			elementType = "sd:restrictedString";
		}
		else if (returnType == boolean.class || returnType == Boolean.class) {
			elementType = "sd:restrictedBoolean";
		}
		else {
			throw new InvalidClassException(String.format(
					"Error in class '%s'. The schema generator does not support properties of type '%s'.",
					type.getSimpleName(), returnType.getSimpleName()));
		}

		return elementType;
	}


	private SortedMap<String, Method> findAnnotatedProperties(Class<?> type, int version)
			throws InvalidClassException {

		SortedMap<String, Method> elements = new TreeMap<String, Method>();

		for (Method method : type.getMethods()) {

			for (Annotation annotation : method.getAnnotations()) {
				// Skip properties that are not set as output.

				if (annotation instanceof Column) {
					Column outputAnnotation = (Column) annotation;

					// Output the property to this version if the version
					// is specified in the list of supported versions or
					// the list of versions is empty.

					boolean enabled = false;

					Versioned versioned = method.getAnnotation(Versioned.class);

					if (versioned != null && versioned.value().length != 0)
						for (int i : versioned.value()) {
							if (i == version) {
								enabled = true;
								break;
							}
						}
					else
						enabled = true;

					if (enabled) {
						String elementName = getElementNameFromMethod(method, outputAnnotation);

						// Elements cannot have the same name!

						if (elements.containsKey(elementName)) {
							String message = String
									.format("The class '%s' contains several output properties that will result in the same element name '%s'.",
											type.getSimpleName(), elementName);
							throw new InvalidClassException(message);
						}

						elements.put(elementName, method);
					}

					// There is no reason to look a the other annotations.

					break;
				}
			}
		}

		return elements;
	}


	protected String getElementNameFromMethod(Method method, Column outputAnnotation) {

		// TODO: Use the entity helper class instead.

		String elementName;

		if (outputAnnotation != null && !outputAnnotation.name().equals("")) {

			elementName = outputAnnotation.name();
		}
		else {
			elementName = method.getName();

			// Remove 'get'-prefix if it is present.

			if (elementName.startsWith("get")) {
				elementName = elementName.substring(3);

				// TODO: Set the first character to lower case.
			}
		}

		// First letter should be lowercase.

		return elementName.substring(0, 1).toLowerCase() + elementName.substring(1);
	}


	protected void write(BufferedWriter writer, int level, String text) throws IOException {

		for (int i = 0; i < level; i++) {
			writer.append('\t');
		}

		writer.write(text + "\n");
	}

}
