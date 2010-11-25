package com.trifork.sdm;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;


/**
 * Reinitializes the schema applying all migrations.
 * 
 * @goal reset
 */
public class ResetDatabaseGoal extends AbstractMojo
{
	/**
	 * The url of the database.
	 * 
	 * @parameter expression="${reset.url}" default-value="jdbc:mysql:///"
	 * @required
	 */
	private String url;
	
	/**
     * The name of the database schema to reset. 
     *
     * @parameter expression="${reset.schema}" default-value="sdm"
     * @required
     */
	private String schema;
	
	/**
     * The connection driver to use.
     *
     * @parameter expression="${reset.schema}" default-value="org.gjt.mm.mysql.Driver"
     * @required
     */
	private String driver;

	/**
     * The username used to connect to the database.
     *
     * @parameter expression="${reset.username}" default-value="root"
     * @required
     */
	private String username;
	
	// TODO: Set root to "" (e.i. the empty string which is the norm).

	/**
	 * The password used to connect to the database.
	 * 
	 * @parameter expression="${reset.password}" default-value="root"
	 * @required
	 */
	private String password;


	public void execute() throws MojoExecutionException
	{
		Connection connection = null;
		
		try
		{
			// Simply call the two other goals.
			
			Class.forName(driver).newInstance();
			
			connection = DriverManager.getConnection(url, username, password);

			Statement statement = connection.createStatement();
			
			// TODO: We should not have to drop the database. But I see no other solution.
			
			statement.executeUpdate("DROP DATABASE " + schema);
			
			statement.close();
			
			statement = connection.createStatement();
			
			statement.executeUpdate("CREATE DATABASE " + schema);
			
			statement.close();
			
			//MigrateDatabaseGoal migrateGoal = new MigrateDatabaseGoal(connection);
			
			//migrateGoal.execute();
			
			connection.close();
			
			getLog().info(String.format("Database '%s' was dropped.", new Object[] {schema}));
		}
		catch (Exception e)
		{
			String message = String.format("Could not drop database '%s'", new Object[] {schema});
			
			throw new MojoExecutionException(message, e);
		}
	}
}
