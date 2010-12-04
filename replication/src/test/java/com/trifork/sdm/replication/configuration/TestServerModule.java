package com.trifork.sdm.replication.configuration;

import java.util.EventListener;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;
import com.trifork.sdm.replication.JettyServer;
import com.trifork.sdm.replication.ResourceDispatcher;
import com.trifork.sdm.replication.Server;
import com.trifork.sdm.replication.configuration.properties.AuthorizationTTL;
import com.trifork.sdm.replication.configuration.properties.Host;
import com.trifork.sdm.replication.configuration.properties.PageSize;
import com.trifork.sdm.replication.configuration.properties.Port;

public class TestServerModule extends AbstractModule {

	@Override
	protected void configure() {
		
		bind(Server.class).to(JettyServer.class).in(Scopes.SINGLETON);
		bindConstant().annotatedWith(Host.class).to("0.0.0.0");
		bindConstant().annotatedWith(Port.class).to(3001);
		bind(EventListener.class).to(ResourceDispatcher.class);
		
		
		bindConstant().annotatedWith(PageSize.class).to(1000);
		bindConstant().annotatedWith(AuthorizationTTL.class).to(15);
	}
}
