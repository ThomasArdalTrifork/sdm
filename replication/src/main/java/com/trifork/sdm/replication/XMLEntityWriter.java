package com.trifork.sdm.replication;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;

import com.trifork.sdm.models.Record;


/**
 * Class that given an entity type, can output instances of that entity to an
 * output stream in XML format.
 * 
 * The class uses the information entity's {@link Column} annotations, and the
 * naming convention to infer names.
 */
public class XMLEntityWriter implements EntityWriter {

	private final String collectionStartTag;
	private final String collectionEndTag;
	
	private final static Object[] NO_ARGUMENTS = new Object[] {};

	private final List<EntityEntry> elements = new ArrayList<EntityEntry>();
	private String startTag;
	private String endTag;
	private String entityXMLName;


	/**
	 * Helper class that generates the XML.
	 * 
	 * We might as well generate the start- and end-tags at initialization, that
	 * way we don't have to do it on a per instance basis.
	 */
	private class EntityEntry {

		public String startTag;
		public String endTag;
		
		public Method method;

		public EntityEntry(Method method) {

			this.method = method;

			String name = inferElementName(method);
			startTag = String.format("\t<%s>", name);
			endTag = String.format("</%s>\n", name);
		}


		private String inferElementName(Method method) {

			// FIXME: Move this to a helper that can share code with the schema
			// project.

			String name;

			Column annotation = method.getAnnotation(Column.class);

			if (annotation != null && !annotation.name().isEmpty()) {

				name = annotation.name();
			}
			else {

				name = method.getName();

				// Remove 'get'-prefix if it is present.
				if (name.startsWith("get")) {

					name = name.substring("get".length());
				}
			}

			return name.toLowerCase();
		}
	}


	public XMLEntityWriter(Class<? extends Record> entity) {

		// Calculate all tags.
		
		entityXMLName = entity.getSimpleName().toLowerCase();
		
		endTag = String.format("\t</%s>\n", entityXMLName);
		
		collectionStartTag = String.format("<%sCollection>\n", entityXMLName);
		collectionEndTag = String.format("</%sCollection>\n", entityXMLName);
		
		for (Method method : entity.getMethods()) {

			Column annotation = method.getAnnotation(Column.class);

			if (annotation != null) {

				elements.add(new EntityEntry(method));
			}
		}
	}


	@Override
	public String getContentType() {

		return "application/xml";
	}


	@Override
	public void output(Query query, OutputStream outputStream) throws IOException {

		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream));
		
		writer.write(collectionStartTag);
		
		BigInteger i = new BigInteger("12123128930000000000");
		
		for (Record record : query) {
			
			i = i.add(new BigInteger("1"));
			
			StringBuilder builder = new StringBuilder();
			
			builder.append(String.format("\t<%s recordId=\"%s\" updateToken=\"%s\">\n", entityXMLName, record.getKey(), i.toString()));

			for (EntityEntry entry : elements) {
				
				builder.append("\t");
				builder.append(entry.startTag);

				try {
					Object value = entry.method.invoke(record, NO_ARGUMENTS);

					if (value != null) {
						builder.append("<[CDATA[");
						builder.append(value);
						builder.append("]]>");
					}
				}
				catch (IllegalAccessException e) {
					// TODO: Call failure notifier.
					throw new RuntimeException(e);
				}
				catch (InvocationTargetException e) {
					// TODO: Call failure notifier.
					throw new RuntimeException(e);
				}

				builder.append(entry.endTag);
			}
			
			builder.append("\t\t<validFrom>");
			builder.append(record.getValidFrom());
			builder.append("</validFrom>\n");
			
			builder.append("\t\t<validTo>");
			builder.append(record.getValidTo());
			builder.append("</validTo>\n");
			
			builder.append(endTag);
			
			writer.write(builder.toString());
			writer.flush();
		}
		
		writer.write(collectionEndTag);
		writer.flush();
	}
}
