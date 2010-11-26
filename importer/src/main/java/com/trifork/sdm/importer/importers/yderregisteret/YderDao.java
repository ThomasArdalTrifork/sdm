package com.trifork.sdm.importer.importers.yderregisteret;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.trifork.sdm.importer.persistence.FilePersistException;
import com.trifork.sdm.importer.persistence.mysql.MySQLTemporalDao;


public class YderDao extends MySQLTemporalDao
{
	public YderDao(Connection connection)
	{
		super(connection);
	}


	public int getLastLoebenummer() throws FilePersistException
	{
		int latestInDB = 0;
		
		try
		{
			Statement stm = connection.createStatement();
			ResultSet rs = stm.executeQuery("SELECT MAX(Loebenummer) FROM YderLoebenummer");
			
			if (rs.next())
			{
				latestInDB = rs.getInt(1);
			}
		}
		catch (SQLException sqle)
		{
			try
			{
				connection.close();
			}
			catch (Exception e)
			{/* ignore */
				// TODO: Log?
			}
			throw new FilePersistException("An error occured while querying latest loebenummer "
					+ sqle.getMessage(), sqle);
		}
		
		return latestInDB;
	}


	public void setLastLoebenummer(int loebeNummer) throws FilePersistException
	{
		try
		{
			Statement stm = connection.createStatement();
			stm.execute("INSERT INTO YderLoebenummer (Loebenummer) values (" + loebeNummer + "); ");
		}
		catch (SQLException sqle)
		{
			throw new FilePersistException(
					"Det opstoed en fejl ved skrivning af l¿benummer til databasen under indl¾sning af et yderregister: "
							+ sqle.getMessage(), sqle);
		}
	}
}
