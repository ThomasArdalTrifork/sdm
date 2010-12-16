package com.trifork.sdm.replication.service;

import java.sql.Connection;
import java.sql.DriverManager;

import javax.inject.Inject;

import com.trifork.sdm.replication.configuration.properties.DbPassword;
import com.trifork.sdm.replication.configuration.properties.DbSchema;
import com.trifork.sdm.replication.configuration.properties.DbURL;
import com.trifork.sdm.replication.configuration.properties.DbUsername;


public class MySQLConnectionFactory implements JdbcConnectionFactory {

	@Inject
	@DbURL
	private String url;

	@Inject
	@DbSchema
	private String schema;

	@Inject
	@DbUsername
	private String username;

	@Inject
	@DbPassword
	private String password;

	
	{
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
		}
		catch (Exception e) {
			throw new RuntimeException("Could not load the database driver.", e);
		}
	}


	public Connection create() {

		Connection connection;

		try {
			connection = DriverManager.getConnection(url + schema, username, password);
		}
		catch (Exception e) {
			throw new RuntimeException("Could not open a connection to the database.", e);
		}

		return connection;
	}
}
