package com.trifork.sdm;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;


/**
 * Goal which migrates a database.
 * 
 * @goal migrate
 */
public class MigrateDatabaseGoal extends AbstractMojo
{
	private static final String HORIZONTAL_LINE = "------------------------------------------------------------------------";

	/**
	 * The url of the database.
	 * 
	 * @parameter expression="${migrate.url}" default-value="jdbc:mysql:///"
	 * @required
	 */
	private String url;

	/**
	 * The name of the database schema to migrate.
	 * 
	 * @parameter expression="${migrate.schema}" default-value="sdm"
	 * @required
	 */
	private String schema;

	/**
	 * The connection driver to use.
	 * 
	 * @parameter expression="${migrate.schema}"
	 *            default-value="org.gjt.mm.mysql.Driver"
	 * @required
	 */
	private String driver;

	/**
	 * The username used to connect to the database.
	 * 
	 * @parameter expression="${migrate.username}" default-value="root"
	 * @required
	 */
	private String username;

	// TODO: Set root to "" (e.i. the empty string which is the norm).

	/**
	 * The password used to connect to the database.
	 * 
	 * @parameter expression="${migrate.password}"
	 */
	private String password;

	/**
	 * The directory containing the migrations.
	 * 
	 * @parameter expression="${migrate.directory}"
	 *            default-value="${project.basedir}/src/main/sql/migrations"
	 * @required
	 */
	private String migrationDirectory;


	public void execute() throws MojoExecutionException
	{
		Statement statement = null;
		Connection connection = null;

		try
		{
			// Only make a new connection if the migrate goal is called
			// directly.
			
			Class.forName(driver).newInstance();

			if (password != null) {
				connection = DriverManager.getConnection(url + "mysql", username, password);				
			} else {
				connection = DriverManager.getConnection("jdbc:mysql://localhost/" + schema + "?user=" + username); 
			}

			// Make sure that the schema version table exists.

			DatabaseMetaData dbm = connection.getMetaData();

			ResultSet rs = dbm.getTables(null, null, "schema_version", null);

			if (!rs.next())
			{
				// If the version table does not exist, create it.

				getLog().info("Creating schema version metadata table.");

				statement = connection.createStatement();
				statement.execute("CREATE TABLE schema_version (" + "id INT NOT NULL AUTO_INCREMENT," + "version CHAR(14) NOT NULL," + "PRIMARY KEY (id))");
				statement.close();

				// Insert a dummy version less than all new version numbers.

				statement = connection.createStatement();
				statement.executeUpdate("INSERT INTO schema_version (version) VALUES ('00000000000000')");
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
			
			getLog().info("Current schema version is " + schemaVersion);
			
			// Run through the migrations.
			
			for (String migration : migrations)
			{
				// Skip any files that are not named correctly.

				if (!migration.matches(".*_.*\\.sql")) // TODO: must be formatted starting with time stamp.
				{
					if (!migration.equals(".svn"))
					{
						getLog().warn("Skipping file '" + migration + "'. Filename is not formatted correctly.");
					}
					
					continue;
				}
				
				// Update the version number.

				String migrationVersion = migration.split("_")[0];
				
				// Skip migrations already applied.
				
				if (schemaVersion.compareTo(migrationVersion) >= 0)
				{
					getLog().debug("SKIPPING MIGRATION: " + migrationVersion);
					continue;
				}
				
				schemaVersion = migrationVersion;

				String fileContent = readFileAsString(directory.getAbsolutePath() + "/" + migration);

				// If the SQL file contains several statements we can split it
				// on ';'.
				
				// TODO: This might not be portable to other databases.

				for (String sql : fileContent.split(";"))
				{
					// Skip empty queries.
					if (sql.trim().isEmpty()) continue;
					
					statement = connection.createStatement();
					statement.execute(sql);
					statement.close();
				}

				// TODO: Print only the last part of the migration as a
				// sentence.
				// migration = migration formatted.

				getLog().info("APPLYING MIGRATION: " + migrationVersion);
			}

			// Update the schema version to the highest time stamp.

			if (schemaVersion != null)
			{
				statement = connection.createStatement();
				statement.executeUpdate("UPDATE schema_version SET version = '" + schemaVersion + "'");
				statement.close();
			}

			connection.close();

			getLog().info(HORIZONTAL_LINE);
			getLog().info("DATABASE MIGRATION SUCCESSFUL (SCHEMA VERSION " + schemaVersion + ")");
			getLog().info(HORIZONTAL_LINE);
		}
		catch (Exception e)
		{
			getLog().info(HORIZONTAL_LINE);
			getLog().error("DATABASE MIGRATION FAILED");
			getLog().info(HORIZONTAL_LINE);

			if (connection != null) try
			{
				connection.rollback(); // TODO: This should all be in a transaction.
				connection.close();
			}
			catch (Throwable t)
			{
				// Do nothing.
			}

			throw new MojoExecutionException("Migration Error", e);
		}
	}


	private static String readFileAsString(String filePath) throws java.io.IOException
	{
		byte[] buffer = new byte[(int) new File(filePath).length()];
		BufferedInputStream f = new BufferedInputStream(new FileInputStream(filePath));

		f.read(buffer);

		return new String(buffer);
	}
}
