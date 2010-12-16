package com.trifork.sdm.replication.configuration;

import java.io.InputStream;
import java.util.Properties;

import com.google.inject.servlet.ServletModule;


public abstract class PropertyServletModule extends ServletModule {

	private static Properties properties;

	{
		properties = new Properties();

		try {
			InputStream inputStream = getClass().getClassLoader().getResourceAsStream("config.properties");
			properties.load(inputStream);
			inputStream.close();
		}
		catch (Throwable t) {
			throw new RuntimeException("Could not read the 'config.properties' file.", t);
		}
	}


	protected String property(String propertyName) {
		
		return property(propertyName, "");
	}


	protected String property(String propertyName, String defaultValue) {

		return properties.getProperty(propertyName, defaultValue);
	}
}
