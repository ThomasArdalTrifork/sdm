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
import java.util.Date;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import org.apache.commons.io.FileUtils;
import org.reflections.Reflections;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.scanners.TypeElementsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;

import com.sun.corba.se.impl.util.PackagePrefixChecker;
import com.trifork.sdm.persistence.annotations.Output;


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

		Set<Class<?>> entities = reflections.getTypesAnnotatedWith(Output.class);

		for (Class<?> entity : entities) {

			System.out.println("Entity: " + entity.getSimpleName());

			Output typeAnnotation = entity.getAnnotation(Output.class);

			for (int version : typeAnnotation.supportedVersions()) {

				generateSchemaVersion(entity, typeAnnotation, version);
			}
		}
	}


	private void generateSchemaVersion(Class<?> type, Output typeAnnotation, int version)
			throws IOException {

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

		String schemaFilePath = String.format("%s/%s_v%d.xsd", target, schemaName, version);
		OutputStreamWriter output = new OutputStreamWriter(new FileOutputStream(schemaFilePath));
		BufferedWriter writer = new BufferedWriter(output);

		write(writer, 0, "<?xml version=\"1.0\" encoding=\"utf-8\"?>");

		// Import the Stamdata namespace.

		write(writer, 0, String.format(
				"<xs:schema elementFormDefault=\"unqualified\" xmlns:xs=\"%s\" xmlns:sd=\"%s\">",
				XML_SCHEMA_NAMESPACE, STAMDATA_NAMESPACE));
		write(writer, 1, String.format(
				"<xs:import namespace=\"%s\" schemaLocation=\"Stamdata.xsd\" />",
				STAMDATA_NAMESPACE));

		// Begin the element itself.

		write(writer, 1, String.format("<xs:element name=\"%s\">", schemaName));

		// Write the documentation annotation if it exists.

		if (!typeAnnotation.documentation().isEmpty()) {
			write(writer, 2, "<xs:annotation>");
			write(writer, 3, "<xs:documentation>");
			write(writer, 4, typeAnnotation.documentation());
			write(writer, 3, "</xs:documentation>");
			write(writer, 2, "</xs:annotation>");
		}
		else {
			System.err
					.println(String
							.format("The class '%s' is not documented! Add documentation to the @Output annotation.",
									type.getSimpleName()));
		}

		write(writer, 2, "<xs:complexType>");
		write(writer, 3, "<xs:sequence>");

		// We want the elements to appear in alphabetical order.

		SortedMap<String, Method> elements = findAnnotatedProperties(type, version);

		// Output each of the found element properties to the schema.

		for (Map.Entry<String, Method> entry : elements.entrySet()) {
			String elementName = entry.getKey();
			Method method = entry.getValue();

			String elementType = getElementType(type, method);

			// Create the element in the schema.

			write(writer, 4, String.format("<xs:element name=\"%s\" type=\"%s\" />", elementName,
					elementType));
		}

		writeAuditMethod(type, "getValidFrom", writer);
		writeAuditMethod(type, "getValidTo", writer);
		
		// TODO: These two might get included in the future.
		//writeAuditMethod(type, "getCreatedDate", writer);
		//writeAuditMethod(type, "getModifiedDate", writer);

		write(writer, 3, "</xs:sequence>");
		write(writer, 2, "</xs:complexType>");
		write(writer, 1, "</xs:element>");
		write(writer, 0, "</xs:schema>");

		writer.close();
		output.close();
	}


	private void writeAuditMethod(Class<?> type, String methodName, BufferedWriter writer)
			throws IOException {

		try {

			Method method = type.getMethod(methodName);

			String elementName = getElementNameFromMethod(method, null);

			write(writer, 4, String.format("<xs:element name=\"%s\" type=\"%s\" />", elementName,
					"xs:dateTime"));
		}
		catch (NoSuchMethodException e) {

			String message = String.format("ERROR: Entity '%s' did not have a method named '%s'.",
					type.getSimpleName(), methodName);

			throw new RuntimeException(message, e);
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
			elementType = "sd:secureInteger";
		}
		else if (returnType == double.class || returnType == Double.class
				|| returnType == float.class || returnType == Float.class) {
			elementType = "sd:secureDouble";
		}
		else if (returnType == Date.class) {
			elementType = "sd:secureDate";
		}
		else if (returnType == String.class) {
			elementType = "sd:secureString";
		}
		else if (returnType == boolean.class || returnType == Boolean.class) {
			elementType = "sd:secureBoolean";
		}
		else {
			throw new InvalidClassException(
					String.format(
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

				if (annotation instanceof Output) {
					Output outputAnnotation = (Output) annotation;

					// Output the property to this version if the version
					// is specified in the list of supported versions or
					// the list of versions is empty.

					boolean enabled = false;

					for (int i : outputAnnotation.supportedVersions()) {
						if (i == version) {
							enabled = true;
							break;
						}
					}

					// Special case no supported version is specified,
					// include in all versions.

					if (outputAnnotation.supportedVersions().length == 0) {
						enabled = true;
					}

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


	protected String getElementNameFromMethod(Method method, Output outputAnnotation) {

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

		return elementName.toLowerCase();
	}


	protected void write(BufferedWriter writer, int level, String text) throws IOException {

		for (int i = 0; i < level; i++) {
			writer.append('\t');
		}

		writer.write(text + "\n");
	}

}
