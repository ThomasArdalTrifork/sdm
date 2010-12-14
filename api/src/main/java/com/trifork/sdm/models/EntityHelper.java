package com.trifork.sdm.models;

import static java.lang.String.format;

import java.lang.reflect.Method;

import javax.persistence.Id;


public class EntityHelper {

	public static Method getIdMethod(Class<? extends Record> entity) {

		Method id = null;

		for (Method method : entity.getMethods()) {

			if (method.isAnnotationPresent(Id.class)) {

				id = method;
			}
		}

		assert id != null : format("Entity %s does not have a @Id method.", entity.getSimpleName());

		return id;
	}
}
