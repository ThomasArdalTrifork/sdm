package com.trifork.sdm.replication.configuration;

import java.util.Properties;

import com.trifork.sdm.replication.configuration.properties.AuthorizationTTL;
import com.trifork.sdm.replication.configuration.properties.DefaultPageSize;
import com.trifork.sdm.replication.configuration.properties.Secret;


public class GatewayModule extends PropertyModule {

	private static final String PROPERTY_PAGE_SIZE = "replication.defaults.pageSize";

	private static final String PROPERTY_SECRET = "replication.security.secret";
	private static final String PROPERTY_URL_TTL = "replication.security.urlTimeToLive";


	@Override
	protected void configure(Properties properties) {

		bindConstant().annotatedWith(Secret.class).to(properties.getProperty(PROPERTY_SECRET));
		bindConstant().annotatedWith(DefaultPageSize.class).to(Integer.parseInt(properties.getProperty(PROPERTY_PAGE_SIZE)));
		bindConstant().annotatedWith(AuthorizationTTL.class).to(Integer.parseInt(properties.getProperty(PROPERTY_URL_TTL)));
	}
}
