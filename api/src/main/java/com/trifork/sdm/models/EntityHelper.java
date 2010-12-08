package com.trifork.sdm.models;

import java.lang.reflect.Method;

import javax.persistence.Column;


public final class EntityHelper {

	public static String getPropetyName(Method method) {

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

		return name.toLowerCase();
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

			String message = String.format("No setter-method was present for entity '%s'.",
					entity.getSimpleName());
			throw new RuntimeException(message, e);
		}

		return setter;
	}
}
