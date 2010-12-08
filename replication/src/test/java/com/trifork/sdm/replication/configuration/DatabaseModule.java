package com.trifork.sdm.replication.configuration;

import com.google.inject.AbstractModule;
import com.trifork.sdm.replication.ConnectionManager;
import com.trifork.sdm.replication.MySQLConnectionManager;
import com.trifork.sdm.replication.configuration.properties.DbPassword;
import com.trifork.sdm.replication.configuration.properties.DbSchema;
import com.trifork.sdm.replication.configuration.properties.DbURL;
import com.trifork.sdm.replication.configuration.properties.DbUsername;

public class DatabaseModule extends AbstractModule {

	@Override
	protected void configure() {

		bindConstant().annotatedWith(DbSchema.class).to("sdm");
		bindConstant().annotatedWith(DbUsername.class).to("root");
		bindConstant().annotatedWith(DbPassword.class).to("");
		bindConstant().annotatedWith(DbURL.class).to("jdbc:mysql://localhost/");
		
		bind(ConnectionManager.class).to(MySQLConnectionManager.class);
	}
}
