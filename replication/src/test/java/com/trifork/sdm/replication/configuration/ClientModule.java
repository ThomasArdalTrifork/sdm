package com.trifork.sdm.replication.configuration;

import com.google.inject.AbstractModule;
import com.trifork.sdm.replication.client.Client;


public class ClientModule extends AbstractModule {

	@Override
	protected void configure() {

		bind(Client.class);
	}
}
