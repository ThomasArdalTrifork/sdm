package com.trifork.sdm.importer.monitoring;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import org.apache.log4j.Logger;

import com.trifork.sdm.importer.persistence.mysql.MySQLConnectionManager;


public class DatabaseStatus
{
	private final static Logger logger = Logger.getLogger(DatabaseStatus.class);
	
	public boolean isAlive()
	{
		boolean isAlive = false;
		
		Connection con = null;
		
		try
		{
			con = MySQLConnectionManager.getConnection();
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT 1");
			rs.next();

			if (1 == rs.getInt(1)) isAlive = true;
		}
		catch (Exception e)
		{
			logger.error("Database connection is down.", e);
		}
		finally
		{
			MySQLConnectionManager.close(con);
		}

		return isAlive;
	}
}