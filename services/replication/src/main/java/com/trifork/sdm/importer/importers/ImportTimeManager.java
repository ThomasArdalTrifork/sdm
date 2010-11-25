package com.trifork.sdm.importer.importers;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Calendar;

import org.apache.log4j.Logger;

import com.trifork.sdm.importer.persistence.mysql.MySQLConnectionManager;
import com.trifork.sdm.util.DateUtils;


public class ImportTimeManager
{
	private static Logger logger = Logger.getLogger(ImportTimeManager.class);


	public static Calendar getLastImportTime(String spoolername)
	{
		Connection con = null;
		Statement stmt = null;
		try
		{
			con = MySQLConnectionManager.getAutoCommitConnection();
			stmt = con.createStatement();
			
			ResultSet rs = stmt.executeQuery("select max(importtime) from "
					+ MySQLConnectionManager.getHousekeepingDBName()
					+ ".Import where spoolername = '" + spoolername + "'");
			
			if (rs.next())
			{
				Calendar cal = DateUtils.toCalendar(rs.getTimestamp(1));
				return cal;
			}
			else
				return null;
		}
		catch (Exception e)
		{
			logger.error("getLastImportTime(" + spoolername + ")", e);
			return null;
		}
		finally
		{
			MySQLConnectionManager.close(stmt, con);
		}

	}


	public static void setImportTime(String spoolerName, Calendar importTime)
	{
		Connection con = null;
		Statement stmt = null;
		
		try
		{
			con = MySQLConnectionManager.getAutoCommitConnection();
			stmt = con.createStatement();
			stmt.executeUpdate("insert into " + MySQLConnectionManager.getHousekeepingDBName()
					+ ".Import values('" + DateUtils.toMySQLdate(importTime) + "', '" + spoolerName
					+ "')");
		}
		catch (Exception e)
		{
			logger.error("getLastImportTime(" + spoolerName + ")", e);
		}
		finally
		{
			MySQLConnectionManager.close(stmt, con);
		}

	}

}
