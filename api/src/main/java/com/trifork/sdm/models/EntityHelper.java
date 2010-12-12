package com.trifork.sdm.models;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;


public final class EntityHelper {

	public static String getColumnName(Method method) {

		assert method != null;
		assert method.getName().startsWith("get");

		Column annotation = method.getAnnotation(Column.class);
		assert annotation != null;

		String name;

		if (!annotation.name().equals("")) {

			name = annotation.name();
		}
		else {
			name = method.getName();
			name = name.substring(3);
		}

		return name;
	}


	public static String getPropetyName(Method method) {

		return getColumnName(method).toLowerCase();
	}


	public static Method getSetterFromGetter(Class<?> entity, Method getter) {

		assert entity != null;
		assert getter != null;
		assert getter.isAnnotationPresent(Column.class);
		assert getter.getName().startsWith("get");

		String setterName = "set" + getter.getName().substring(3);
		Method setter;

		try {
			setter = entity.getMethod(setterName, getter.getReturnType());
		}
		catch (NoSuchMethodException e) {

			String message = String.format("No setter-method was present for '%s#%s'.",
					entity.getSimpleName(), getter.getName());
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


	public static List<Method> getOutputMethods(Class<? extends Record> type) {
	
		List<Method> outputMethods = new ArrayList<Method>();
	
		for (Method method : type.getMethods()) {
	
			if (!method.isAnnotationPresent(Column.class)) continue;
			outputMethods.add(method);
		}
	
		return outputMethods;
	}


	/**
	 * TODO Document this method.
	 * 
	 * @param method
	 *            A getter method, that is used for serialization.
	 * @return The name used to designate this field when serializing
	 */
	public static String getOutputFieldName(Method method) {
	
		// TODO: Use the entity helper class.
	
		String name = AbstractRecord.outputFieldNames.get(method);
	
		if (name == null) {
	
			Column output = method.getAnnotation(Column.class);
	
			// Strip "get"
	
			name = method.getName().substring(3);
			if (output != null && output.name().length() > 0) {
				name = output.name();
			}
	
			AbstractRecord.outputFieldNames.put(method, name);
		}
	
		return name;
	}


	/**
	 * TODO: What does it do?
	 * 
	 * @param class1
	 *            A type of Entity.
	 * 
	 * @return the getter method that contains the unique id for the given
	 *         Entity type
	 */
	public static Method getIdMethod(Class<? extends Record> class1) {
	
		Method m = AbstractRecord.idMethodCache.get(class1);
	
		if (m != null) return m;
	
		Method[] allMethods = class1.getMethods();
	
		for (Method method : allMethods) {
			if (method.isAnnotationPresent(Id.class)) {
				AbstractRecord.idMethodCache.put(class1, method);
				return method;
			}
		}
	
		AbstractRecord.logger.error("Could not find idmethod for class: " + class1 + " A getter must be annotated with @Id!");
	
		return null;
	}
}
