package com.trifork.sdm.importer.persistence.mysql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

import org.apache.log4j.Logger;

import com.trifork.sdm.importer.configuration.Configuration;


public class MySQLConnectionManager {

	private static Logger logger = Logger.getLogger(MySQLConnectionManager.class);

	// TODO: Consolidate the schemas.
	private static final String HOUSEKEEPING_DATABASE_KEY = "db.housekeepingdatabase";

	private static final String DATABASE_DRIVER_KEY = "db.driver";
	private static final String DATABASE_NAME_KEY = "db.schema";
	private static final String DATABASE_PASSWORD_KEY = "db.password";
	private static final String DATABASE_USER_KEY = "db.username";
	private static final String DATABASE_URL_KEY = "db.url";


	public static Connection getConnection() {

		Connection connection = null;

		try {
			
			// Load the database driver.
			
			String driver = Configuration.getString(DATABASE_DRIVER_KEY);
			Class.forName(driver).newInstance();

			// Connect to the database.

			String url = Configuration.getString(DATABASE_URL_KEY);
			String username = Configuration.getString(DATABASE_USER_KEY);
			String password = Configuration.getString(DATABASE_PASSWORD_KEY);

			connection = DriverManager.getConnection(url + getDBName(), username, password);

			connection.setAutoCommit(false);
		}
		catch (Exception e) {
			
			logger.error("Error creating MySQL database connection", e);

			// COMMENT (thb): Should we not fail completely here and exit the
			// application?
		}

		return connection;
	}


	public static Connection getAutoCommitConnection() {

		Connection connection = null;
		
		try {

			connection = getConnection();
			connection.setAutoCommit(true);
		}
		catch (Exception e) {

			logger.error("Error creating MySQL database connection.", e);
		}
		
		return connection;
	}


	public static String getDBName() {

		return Configuration.getString(DATABASE_NAME_KEY);
	}


	public static String getHousekeepingDBName() {

		// TODO: Should not be needed anymore.
		String confName = Configuration.getString(HOUSEKEEPING_DATABASE_KEY);
		return (confName != null) ? confName : getDBName() + "_housekeeping";
	}


	public static void close(Connection connection) {

		// TODO: We should notify someone about these errors.

		try {
			if (connection != null) {
				
				connection.close();
			}
			else {
				
				logger.warn("Cannot commit and close connection, because connection == null");
			}
		}
		catch (Exception e) {
			
			logger.error("Could not close connection", e);
		}
	}


	public static void close(Statement stmt, Connection connection) {

		try {
			
			if (stmt != null) {
				
				stmt.close();
			}
			else {
				
				logger.warn("Cannot close stmt, because stement == null.");
			}
		}
		catch (Exception e) {
			
			logger.error("Could not close statement.", e);
		}
		finally {
			
			close(connection);
		}
	}

}
