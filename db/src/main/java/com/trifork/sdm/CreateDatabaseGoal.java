package com.trifork.sdm;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;


/**
 * Goal which creates a database.
 * 
 * @goal create
 */
public class CreateDatabaseGoal extends AbstractMojo
{
	/**
	 * The url of the database.
	 * 
	 * @parameter expression="${create.url}" default-value="jdbc:mysql://localhost/"
	 * @required
	 */
	private String url;
	
	/**
     * The name of the database schema to create. 
     *
     * @parameter expression="${create.schema}" default-value="sdm"
     */
	private String schema;
	
	/**
     * The connection driver to use.
     *
     * @parameter expression="${create.schema}" default-value="org.gjt.mm.mysql.Driver"
     */
	private String driver;

	/**
     * The username used to connect to the database.
     *
     * @parameter expression="${create.username}" default-value="root"
     */
	private String username;
	
	// TODO: Set root to "" (e.i. the empty string which is the norm).
	
	/**
     * The password used to connect to the database.
     *
     * @parameter expression="${create.password}"
     */
	private String password;


	public void execute() throws MojoExecutionException
	{
		Connection connection = null;
		Statement statement = null;

		try
		{
			Class.forName(driver).newInstance();
			
			if (password != null) {
				connection = DriverManager.getConnection(url + "mysql", username, password);				
			} else {
				connection = DriverManager.getConnection("jdbc:mysql://localhost/mysql" + "?user=" + username); 
			}

			// Create the schema.
			
			statement = connection.createStatement();
			
			statement.executeUpdate("CREATE DATABASE " + schema);

			statement.close();
			/*
			// Create the migration_version table.
			
			statement = connection.createStatement();
			
			// CHAR(14) because a migration time-stamp 
			// will be something like 20100906120000.
			
			
			statement = connection.createStatement();
			
			statement.executeUpdate("INSERT INTO schema_version (version) VALUES (00000000000000)");
			
			statement.close();
			*/
			connection.close();
			
			getLog().info(String.format("Database '%s' was created.", new Object[] {schema}));
		}
		catch (Exception e)
		{
			String message = String.format("Could not create database '%s'", new Object[] {schema});
			
			throw new MojoExecutionException(message, e);
		}
	}
}
