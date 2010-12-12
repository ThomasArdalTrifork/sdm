package com.trifork.sdm.replication.configuration;

import com.google.inject.AbstractModule;
import com.trifork.sdm.replication.configuration.properties.AuthorizationTTL;
import com.trifork.sdm.replication.configuration.properties.PageSize;
import com.trifork.sdm.replication.configuration.properties.Secret;


public class ConfigurationModule extends AbstractModule {

	@Override
	protected void configure() {

		// Load configuration file.

		// Properties properties = loadProperties();

		// Token Settings

		bindConstant().annotatedWith(Secret.class).to("secret");

		bindConstant().annotatedWith(PageSize.class).to(1000);
		bindConstant().annotatedWith(AuthorizationTTL.class).to(15);
	}

/*
	public Properties loadProperties() {

		// Load properties from the 'config.properties' file.

		Properties properties = new Properties();
		InputStream inputStream = null;

		try {
			inputStream = getClass().getClassLoader().getResourceAsStream("config.properties");
			properties.load(inputStream);
			inputStream.close();
		}
		catch (Throwable t) {
			throw new RuntimeException("Could not read the 'config.properties' file.", t);
		}

		return properties;
	}
	*/
}
