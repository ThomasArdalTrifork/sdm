package com.trifork.sdm.models;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Id;

import org.apache.log4j.Logger;

import com.trifork.sdm.util.DateUtils;


public abstract class AbstractRecord implements Record {

	public static final Calendar FUTURE = DateUtils.FUTURE;

	private static final Logger logger = Logger.getLogger(AbstractRecord.class);

	private static final Map<Class<? extends Record>, Method> idMethodCache = new HashMap<Class<? extends Record>, Method>();
	private static final Map<Method, String> outputFieldNames = new HashMap<Method, String>();


	public Object getRecordId() {

		Method idMethod = getIdMethod(getClass());

		try {
			return idMethod.invoke(this);
		}
		catch (Exception e) {
			logger.error("Error getting id for object of class: " + getClass());
			return null;
		}
	}


	public Map<String, Object> serialize() {

		Map<String, Object> map = new HashMap<String, Object>();
		try {
			List<Method> outputMethods = getOutputMethods(getClass());
			for (Method method : outputMethods) {
				map.put(getOutputFieldName(method), method.invoke(this));
			}
		}
		catch (Exception e) {
			logger.error("Error serializing object of class: " + getClass() + " id: " + getRecordId());
		}
		return map;
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

		Method m = idMethodCache.get(class1);

		if (m != null) return m;

		Method[] allMethods = class1.getMethods();

		for (Method method : allMethods) {
			if (method.isAnnotationPresent(Id.class)) {
				idMethodCache.put(class1, method);
				return method;
			}
		}

		logger.error("Could not find idmethod for class: " + class1 + " A getter must be annotated with @Id!");

		return null;
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

		String name = outputFieldNames.get(method);

		if (name == null) {

			Column output = method.getAnnotation(Column.class);

			// Strip "get"

			name = method.getName().substring(3);
			if (output != null && output.name().length() > 0) {
				name = output.name();
			}

			outputFieldNames.put(method, name);
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


	public Calendar getValidTo() {

		return DateUtils.FUTURE;
	}


	public abstract Calendar getValidFrom();
}
