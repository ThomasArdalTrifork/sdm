package com.trifork.sdm.replication.configuration;

import java.util.Date;

import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.trifork.sdm.models.Record;
import com.trifork.sdm.replication.service.JdbcConnectionFactory;
import com.trifork.sdm.replication.service.MySQLConnectionFactory;
import com.trifork.sdm.replication.service.MySQLQuery;
import com.trifork.sdm.replication.service.Query;


public class DatabaseModule extends AbstractModule {

	@Override
	public void configure() {

		bind(JdbcConnectionFactory.class).to(MySQLConnectionFactory.class);

		install(new FactoryModuleBuilder().implement(Query.class, MySQLQuery.class).build(QueryFactory.class));
	}


	public interface QueryFactory {

		Query create(Class<? extends Record> entity, long recordId, Date since);
	}
}
