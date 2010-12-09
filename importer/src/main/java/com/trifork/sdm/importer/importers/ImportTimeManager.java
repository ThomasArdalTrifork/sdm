package com.trifork.sdm.importer.importers;

import static java.lang.String.format;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;

import org.apache.log4j.Logger;

import com.trifork.sdm.importer.persistence.mysql.MySQLConnectionManager;

public class ImportTimeManager {

	private static Logger logger = Logger.getLogger(ImportTimeManager.class);


	public static Date getLastImportTime(String spoolername) {

		Connection connection = null;
		PreparedStatement statement = null;

		try {
			connection = MySQLConnectionManager.getAutoCommitConnection();
			statement = connection.prepareStatement(format("SELECT MAX(importtime) FROM %s.Import WHERE spoolername = ?", MySQLConnectionManager.getHousekeepingDBName()));

			ResultSet rs = statement.executeQuery();

			if (rs.next()) {
				
				return new Date(rs.getTimestamp(1).getTime());
			}
			else
				return null;
		}
		catch (Exception e) {
			
			logger.error("getLastImportTime(" + spoolername + ")", e);
			return null;
		}
		finally {
			
			MySQLConnectionManager.close(statement, connection);
		}
	}


	public static void setImportTime(String spoolerName, Date importTime) {

		Connection connection = null;
		PreparedStatement stmt = null;

		try {
			String schema = MySQLConnectionManager.getHousekeepingDBName();
			
			connection = MySQLConnectionManager.getAutoCommitConnection();
			
			stmt = connection.prepareStatement(format("INSERT INTO %s.Import VALUES (?, ?)", schema));
			
			stmt.setDate(1, new java.sql.Date(importTime.getTime()));
			stmt.setString(2, spoolerName);
		}
		catch (Exception e) {
			
			logger.error("getLastImportTime(" + spoolerName + ")", e);
		}
		finally {
			
			MySQLConnectionManager.close(stmt, connection);
		}
	}

}
