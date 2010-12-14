package com.trifork.sdm.models;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Table;


public class NamingConvention {

	public static String getColumnName(Method method) {

		assert method != null;
		assert method.getName().startsWith("get");

		Column annotation = method.getAnnotation(Column.class);

		String name;

		if (annotation != null && !annotation.name().isEmpty()) {

			name = annotation.name();
		}
		else {
			name = method.getName();
			name = name.substring(3);
		}

		return name;
	}


	public static Method getSetterFromGetter(Class<? extends Record> entity, Method getter) {

		assert entity != null; // TODO: Use preconditions.
		assert getter != null;
		assert getter.isAnnotationPresent(Column.class);
		assert getter.getName().startsWith("get");

		String setterName = "set" + getter.getName().substring(3);
		Method setter;

		try {
			setter = entity.getMethod(setterName, getter.getReturnType());
		}
		catch (NoSuchMethodException e) {

			String message = String.format("No setter-method was present for '%s#%s'.", entity.getSimpleName(), getter.getName());
			throw new RuntimeException(message, e);
		}

		return setter;
	}


	public static String getTableName(Class<? extends Record> entity) {

		String name;

		Table annotation = entity.getAnnotation(Table.class);

		if (annotation != null && !annotation.name().isEmpty()) {
			name = annotation.name();
		}
		else {
			name = entity.getSimpleName();
		}

		return name;
	}


	public static List<Method> getColumns(Class<? extends Record> type) {

		List<Method> columns = new ArrayList<Method>();

		for (Method method : type.getMethods()) {

			if (method.isAnnotationPresent(Column.class)) {
				columns.add(method);
			}
		}

		return columns;
	}	
}
