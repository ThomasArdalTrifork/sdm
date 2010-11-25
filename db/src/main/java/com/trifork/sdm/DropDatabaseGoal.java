package com.trifork.sdm;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;


/**
 * Goal which creates a database.
 * 
 * @goal drop
 */
public class DropDatabaseGoal extends AbstractMojo
{
	/**
	 * The URL of the database.
	 * 
	 * @parameter expression="${drop.url}" default-value="jdbc:mysql:///"
	 */
	private String url;

	/**
	 * The name of the database schema to drop.
	 * 
	 * @parameter expression="${drop.url}" default-value="sdm"
	 */
	private String schema;

	/**
	 * The connection driver to use.
	 * 
	 * @parameter expression="${drop.schema}" default-value="org.gjt.mm.mysql.Driver"
	 */
	private String driver;

	/**
	 * The username used to connect to the database.
	 * 
	 * @parameter expression="${drop.username}" default-value="root"
	 */
	private String username;

	// TODO: Set root to "" (e.i. the empty string which is the norm).

	/**
	 * The password used to connect to the database.
	 * 
	 * @parameter expression="${drop.password}"
	 */
	private String password;


	public void execute() throws MojoExecutionException
	{
		Statement statement = null;
		Connection connection = null;

		try
		{
			Class.forName(driver).newInstance();
			
			if (password != null) {
				connection = DriverManager.getConnection(url + "mysql", username, password);				
			} else {
				connection = DriverManager.getConnection("jdbc:mysql://localhost/mysql" + "?user=" + username); 
			}

			statement = connection.createStatement();

			statement.executeUpdate("DROP DATABASE " + schema);

			statement.close();
			connection.close();

			getLog().info(String.format("Database '%s' was dropped.", new Object[] { schema }));
		}
		catch (Exception e)
		{
			String message = String.format("Could not drop database '%s'", new Object[] { schema });

			throw new MojoExecutionException(message, e);
		}
		
		// FIXME: close safely in finally.
	}
}
