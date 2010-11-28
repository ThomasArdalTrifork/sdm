package com.trifork.sdm;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class DatabaseHelper {

	private static final String MYSQL_DEFAULT_SCHEMA = "mysql";

	/**
	 * The url of the database.
	 */
	private String url;

	/**
	 * The name of the database schema to create.
	 */
	private String schema;
	
	/**
	 * The name of the database schema to create.
	 */
	private String housekeeping_schema; // TODO: REMOVE

	/**
	 * The user name used to connect to the database.
	 */
	private String username;

	/**
	 * The password used to connect to the database.
	 */
	private String password;

	// TODO: Document.
	private String migrationDirectory;
	
	
	public static void main(String[] args) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		
		// TODO: The methods should be called directly from Groovy.
		
		final String action = args[0];
		
		DatabaseHelper helper = new DatabaseHelper();
		
		helper.schema = args[1];
		helper.username = args[2];
		helper.password = args[3];
		helper.url = args[4];
		
		// TODO: Generate error message if this in an invalid driver class.
		final String driver = args[5];
		Class.forName(driver).newInstance();
		
		if (action.equals("create")) {
			
			helper.housekeeping_schema = args[6]; // TODO: REMOVE
			helper.createDB();
		}
		else if (action.equals("drop")) {
			
			helper.housekeeping_schema = args[6]; // TODO: REMOVE
			helper.dropDB();
		}
		else if (action.equals("migrate")) {
			
			helper.migrationDirectory = args[6];
			helper.migrateDB();
		}
		else {
			throw new IllegalArgumentException(String.format("Unknown action '%s'.", action));
		}
	}


	public void createDB() {

		executeSQL("CREATE SCHEMA " + schema, MYSQL_DEFAULT_SCHEMA);
		executeSQL("CREATE SCHEMA " + housekeeping_schema, MYSQL_DEFAULT_SCHEMA); // TODO: Remove
	}


	public void dropDB() {

		executeSQL("DROP SCHEMA IF EXISTS " + schema, MYSQL_DEFAULT_SCHEMA);
		executeSQL("DROP SCHEMA IF EXISTS " + housekeeping_schema, MYSQL_DEFAULT_SCHEMA); // TODO: Remove
	}


	public void migrateDB() {

		Connection connection = null;
		Statement statement = null;

		try {
			
			connection = getConnection(schema);

			// Make sure that the schema version table exists.

			DatabaseMetaData dbm = connection.getMetaData();
			ResultSet rs = dbm.getTables(null, null, "schema_version", null);

			if (!rs.next()) {

				// If the version table does not exist, create it.

				statement = connection.createStatement();
				statement.execute("CREATE TABLE schema_version ("
						+ "id INT NOT NULL AUTO_INCREMENT," + "version CHAR(14) NOT NULL,"
						+ "PRIMARY KEY (id))");
				statement.close();

				// Insert a dummy version.

				statement = connection.createStatement();
				statement.executeUpdate("INSERT INTO schema_version (version) VALUES ('0')");
				statement.close();
			}

			// Find and apply all new migrations.

			File directory = new File(migrationDirectory);

			// Sort the migrations after date.

			String[] migrations = directory.list();
			java.util.Arrays.sort(migrations);

			// Read the current migration number from the database.

			statement = connection.createStatement();
			ResultSet versionResult = statement.executeQuery("SELECT version FROM schema_version");
			versionResult.first();
			String schemaVersion = versionResult.getString("version");
			statement.close();

			// Run through the migrations.

			for (String migration : migrations) {

				// Skip any files that are not named correctly.

				// TODO: must be formatted starting with time stamp.

				if (!migration.matches(".*_.*\\.sql")) {

					if (!migration.equals(".svn")) {

						String message = String.format(
								"Skipping file '%s'. File name is not formatted correctly.",
								migration);
						System.out.println(message);
					}

					continue;
				}

				// Update the version number.

				String migrationVersion = migration.split("_")[0];

				// Skip migrations already applied.

				if (schemaVersion.compareTo(migrationVersion) >= 0) {

					continue;
				}

				schemaVersion = migrationVersion;

				String fileContent = readFileAsString(directory.getAbsolutePath() + "/" + migration);

				// If the SQL file contains several statements we can split it
				// on ';'.

				// TODO: This might not be portable to other databases.

				for (String sql : fileContent.split(";")) {
					// Skip empty queries.
					if (sql.trim().isEmpty()) continue;

					statement = connection.createStatement();
					statement.execute(sql);
					statement.close();
				}

				// TODO: Print only the last part of the migration as a
				// sentence.
				// migration = migration formatted.

				System.out.println("Applying Migration: " + migration);
			}

			// Update the schema version to the highest time stamp.

			if (schemaVersion != null) {
				statement = connection.createStatement();
				statement.executeUpdate("UPDATE schema_version SET version = '" + schemaVersion + "'");
				statement.close();
			}

			connection.close();

			System.out.println("Database migration successful! v" + schemaVersion + ".");
		}
		catch (Exception e) {

			System.err.println("Database migration failed!");
			e.printStackTrace();

			if (connection != null) try {
				connection.rollback();
			}
			catch (Throwable t) {}
		}
		finally {

			if (connection != null) try {
				connection.close();
			}
			catch (Throwable t) {}
		}
	}


	private void executeSQL(String sql, String schema) {

		Connection connection = null;
		Statement statement = null;

		try {

			connection = getConnection(schema);

			statement = connection.createStatement();

			statement.executeUpdate(sql);
		}
		catch (Exception e) {

			String message = String.format("Could not execute SQL '%s'.", sql);
			System.err.println(message);
			e.printStackTrace();
		}
		finally {

			try {

				if (connection != null) connection.close();
			}
			catch (Throwable t) {}
		}
	}


	private static String readFileAsString(String filePath) throws java.io.IOException {

		byte[] buffer = new byte[(int) new File(filePath).length()];
		BufferedInputStream f = new BufferedInputStream(new FileInputStream(filePath));

		f.read(buffer);

		return new String(buffer);
	}


	private Connection getConnection(String schema) throws SQLException {

		Connection connection = null;

		if (password != null) {

			connection = DriverManager.getConnection(url + schema, username, password);
		}
		else {

			// If no password is supplied we construct our own
			// connection URL.

			connection = DriverManager.getConnection(url + schema + "?user=" + username);
		}

		return connection;
	}
}
