package com.trifork.sdm.importer.importers;

import static java.lang.String.format;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
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
			statement = connection.prepareStatement(format("SELECT max(importtime) FROM %s.Import WHERE spoolername = ?", MySQLConnectionManager.getHousekeepingDBName()));

			statement.setString(1, spoolername);
			
			ResultSet rs = statement.executeQuery();

			if (rs.next()) {
				
				return rs.getTimestamp(1);
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
			
			stmt.setTimestamp(1, new Timestamp(importTime.getTime()));
			stmt.setString(2, spoolerName);
			
			stmt.execute();
		}
		catch (Exception e) {
			
			logger.error("getLastImportTime(" + spoolerName + ")", e);
		}
		finally {
			
			MySQLConnectionManager.close(stmt, connection);
		}
	}

}
