package com.trifork.sdm.replication.configuration;

import java.util.EventListener;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;
import com.trifork.sdm.replication.JettyServer;
import com.trifork.sdm.replication.Server;
import com.trifork.sdm.replication.configuration.properties.Host;
import com.trifork.sdm.replication.configuration.properties.Port;

public class ServerModule extends AbstractModule {
	
	private final String host;
	private final int port;

	public ServerModule(String host, int port) {
		this.host = host;
		this.port = port;
	}
	
	@Override
	protected void configure() {

		bind(Server.class).to(JettyServer.class).in(Scopes.SINGLETON);
		bindConstant().annotatedWith(Host.class).to(host);
		bindConstant().annotatedWith(Port.class).to(port);
		bind(EventListener.class).to(ResourceDispatcher.class);
	}
}
