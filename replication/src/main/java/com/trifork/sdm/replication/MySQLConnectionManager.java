package com.trifork.sdm.replication;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.inject.Inject;

import com.trifork.sdm.replication.configuration.properties.DbPassword;
import com.trifork.sdm.replication.configuration.properties.DbSchema;
import com.trifork.sdm.replication.configuration.properties.DbURL;
import com.trifork.sdm.replication.configuration.properties.DbUsername;


public class MySQLConnectionManager implements ConnectionManager {

	private final String schema;
	private final String username;
	private final String password;
	private final String url;

	@Inject
	MySQLConnectionManager(@DbSchema String schema, @DbUsername String username, @DbPassword String password, @DbURL String url) {

		this.schema = schema;
		this.username = username;
		this.password = password;
		this.url = url;

		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}


	@Override
	public Connection getConnection() {

		Connection connection;

		try {
			connection = DriverManager.getConnection(url + schema, username, password);
		}
		catch (Exception e) {

			throw new RuntimeException(e);
		}

		return connection;
	}


	@Override
	public void close(Connection connection) {

		assert connection != null;

		try {
			connection.close();
		}
		catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
}
