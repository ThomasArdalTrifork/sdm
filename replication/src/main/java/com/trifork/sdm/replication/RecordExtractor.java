package com.trifork.sdm.replication;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;

import com.trifork.sdm.models.EntityHelper;
import com.trifork.sdm.models.Record;


public class RecordExtractor {

	private final List<EntityEntry> elements = new ArrayList<EntityEntry>();
	private Class<? extends Record> entity;


	public RecordExtractor(Class<? extends Record> entity) {

		this.entity = entity;
		
		for (Method getter : entity.getMethods()) {

			Column annotation = getter.getAnnotation(Column.class);

			if (annotation != null) {

				elements.add(new EntityEntry(getter));
			}
		}
	}


	public Record extract(ResultSet row) throws IllegalArgumentException, IllegalAccessException,
			InvocationTargetException, SQLException {

		Record record;

		try {
			record = entity.newInstance();
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}

		for (EntityEntry entry : elements) {

			if (entry.parameterType.isAssignableFrom(String.class)) {

				String s = row.getString(entry.columnName);
				entry.setter.invoke(record, s);
			}
			else if (entry.parameterType.isAssignableFrom(Date.class)) {

				Timestamp t = row.getTimestamp(entry.columnName);
				entry.setter.invoke(record, new Date(t.getTime()));
			}
			else if (entry.parameterType.isAssignableFrom(int.class)) {

				int i = row.getInt(entry.columnName);
				entry.setter.invoke(record, i);
			}
			else if (entry.parameterType.isAssignableFrom(long.class)) {

				long l = row.getLong(entry.columnName);
				entry.setter.invoke(record, l);
			}
			else if (entry.parameterType.isAssignableFrom(boolean.class)) {

				boolean b = row.getBoolean(entry.columnName);
				entry.setter.invoke(record, b);
			}
			else {

				throw new RuntimeException("Unsupported Extractor Column Type: " + entry.parameterType.getSimpleName());
			}
		}

		return record;
	}


	/**
	 * Helper class that generates the XML.
	 * 
	 * We might as well generate the start- and end-tags at initialization, that
	 * way we don't have to do it on a per instance basis.
	 */
	protected class EntityEntry {

		public String columnName;
		public Method setter;
		public Class<?> parameterType;


		public EntityEntry(Method getter) {

			this.setter = EntityHelper.getSetterFromGetter(entity, getter);
			this.columnName = EntityHelper.getColumnName(getter);
			this.parameterType = setter.getParameterTypes()[0];
		}
	}
}
