package com.trifork.sdm.replication.configuration;

import java.util.Date;

import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.trifork.sdm.models.Record;
import com.trifork.sdm.replication.configuration.properties.DbPassword;
import com.trifork.sdm.replication.configuration.properties.DbSchema;
import com.trifork.sdm.replication.configuration.properties.DbURL;
import com.trifork.sdm.replication.configuration.properties.DbUsername;
import com.trifork.sdm.replication.service.JdbcConnectionFactory;
import com.trifork.sdm.replication.service.MySQLConnectionFactory;
import com.trifork.sdm.replication.service.MySQLQuery;
import com.trifork.sdm.replication.service.Query;


public class DatabaseModule extends PropertyModule {

	@Override
	public void configure() {

		bindConstant().annotatedWith(DbURL.class).to(property("db.url"));
		bindConstant().annotatedWith(DbSchema.class).to(property("db.schema"));
		bindConstant().annotatedWith(DbUsername.class).to(property("db.username"));
		bindConstant().annotatedWith(DbPassword.class).to(property("db.password"));
		
		bind(JdbcConnectionFactory.class).to(MySQLConnectionFactory.class);

		install(new FactoryModuleBuilder().implement(Query.class, MySQLQuery.class).build(QueryFactory.class));
	}


	public interface QueryFactory {

		Query create(Class<? extends Record> entity, long recordId, Date since);
	}
}
