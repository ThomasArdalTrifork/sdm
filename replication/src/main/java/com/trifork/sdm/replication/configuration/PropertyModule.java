package com.trifork.sdm.replication.configuration;

import java.io.InputStream;
import java.util.Properties;

import com.google.inject.AbstractModule;


public abstract class PropertyModule extends AbstractModule {

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


	@Override
	protected final void configure() {

		configure(properties);
	}


	protected abstract void configure(Properties properties);
}
