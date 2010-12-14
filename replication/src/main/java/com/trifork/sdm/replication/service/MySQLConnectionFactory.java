package com.trifork.sdm.replication.service;

import java.sql.Connection;
import java.sql.DriverManager;


public class MySQLConnectionFactory implements JdbcConnectionFactory {

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
			connection = DriverManager.getConnection("jdbc:mysql://localhost/sdm", "root", "");
		}
		catch (Exception e) {
			throw new RuntimeException("Could not open a connection to the database.", e);
		}

		return connection;
	}
}
