package com.trifork.sdm.replication;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.persistence.Column;


/**
 * Class that given an entity type, can output instances of that entity to an
 * output stream in XML format.
 * 
 * The class uses the information entity's {@link Column} annotations, and the
 * naming convention to infer names.
 */
public class XMLEntityWriter implements EntityWriter {

	private final String startTag;
	private final String endTag;

	private final List<EntityEntry> elements = new ArrayList<EntityEntry>();


	/**
	 * Helper class that generates the XML.
	 * 
	 * We might as well generate the start- and end-tags at initialization, that
	 * way we don't have to do it on a per instance basis.
	 */
	private class EntityEntry {

		public String startTag;
		public String name;
		public String endTag;
		public Method method;


		public EntityEntry(Method method) {

			this.method = method;

			name = inferElementName(method);
			startTag = String.format("\t<%s>", name);
			endTag = String.format("</%s>\n", name);
		}


		private String inferElementName(Method method) {

			// TODO: Move this to a helper that can share code with the schema
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


	public XMLEntityWriter(Class<?> entity) {

		// Calculate all tags.
		
		String entityXMLName = entity.getSimpleName().toLowerCase();
		
		startTag = String.format("<%s>\n", entityXMLName);
		endTag = String.format("</%s>\n", entityXMLName);

		for (Method method : entity.getMethods()) {

			Column annotation = method.getAnnotation(Column.class);

			if (annotation != null) {

				elements.add(new EntityEntry(method));
			}
		}

		// The SDM schema convention says that elements must appear
		// in alphanumerical order.

		Collections.sort(elements, new Comparator<EntityEntry>() {

			@Override
			public int compare(EntityEntry element1, EntityEntry element2) {

				return element1.name.compareTo(element2.name);
			}
		});
	}


	public void write(Object instance, OutputStream outputStream) throws IOException {

		final Object[] NO_ARGUMENTS = new Object[] {};

		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream));
		StringBuilder builder = new StringBuilder();

		builder.append(startTag);

		for (EntityEntry entry : elements) {

			builder.append(entry.startTag);

			try {

				Object value = entry.method.invoke(instance, NO_ARGUMENTS);

				if (value != null) {
					builder.append(String.format("%s", value.toString()));
				}
			}
			catch (IllegalAccessException e) {
				// TODO: Call failure notifier.
			}
			catch (InvocationTargetException e) {
				// TODO: Call failure notifier.
			}

			builder.append(entry.endTag);
		}

		builder.append(endTag);

		writer.write(builder.toString());
		writer.flush();
	}


	@Override
	public String getContentType() {

		return "application/xml";
	}
}
