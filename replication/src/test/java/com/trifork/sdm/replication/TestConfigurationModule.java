package com.trifork.sdm.replication;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;
import com.trifork.sdm.replication.configuration.properties.AuthorizationTTL;
import com.trifork.sdm.replication.configuration.properties.Host;
import com.trifork.sdm.replication.configuration.properties.PageSize;
import com.trifork.sdm.replication.configuration.properties.Port;

public class TestConfigurationModule extends AbstractModule {

	@Override
	protected void configure() {
		
		bindConstant().annotatedWith(Host.class).to("0.0.0.0");
		bindConstant().annotatedWith(Port.class).to(3001);
		
		bind(Server.class).to(JettyServer.class).in(Scopes.SINGLETON);
		
		bindConstant().annotatedWith(PageSize.class).to(1000);
		bindConstant().annotatedWith(AuthorizationTTL.class).to(15);
	}
}
