package com.trifork.sdm.replication.configuration;

import java.security.SecureRandom;

import com.trifork.sdm.replication.configuration.properties.AuthorizationTTL;
import com.trifork.sdm.replication.configuration.properties.DefaultPageSize;
import com.trifork.sdm.replication.configuration.properties.Secret;
import com.trifork.sdm.replication.service.GatewayServlet;


public class GatewayModule extends PropertyServletModule {

	private static final String PROPERTY_PAGE_SIZE = "replication.pageSize";
	private static final String PROPERTY_SECRET = "replication.secret";
	private static final String PROPERTY_URL_TTL = "replication.urlTimeToLive";


	@Override
	protected void configureServlets() {
		
		SecureRandom random = new SecureRandom();
		byte[] secret = new byte[512];
		random.nextBytes(secret);

		bindConstant().annotatedWith(Secret.class).to(property(PROPERTY_SECRET, new String(secret)));
		bindConstant().annotatedWith(DefaultPageSize.class).to(Integer.parseInt(property(PROPERTY_PAGE_SIZE)));
		bindConstant().annotatedWith(AuthorizationTTL.class).to(Integer.parseInt(property(PROPERTY_URL_TTL)));
	
		serve("/gateway").with(GatewayServlet.class);
	}
}
