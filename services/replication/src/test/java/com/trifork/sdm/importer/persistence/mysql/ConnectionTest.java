package com.trifork.sdm.importer.persistence.mysql;

import java.sql.Connection;

import junit.framework.TestCase;



public class ConnectionTest extends TestCase
{
	public void testConnection() throws Exception
	{
		Connection con = MySQLConnectionManager.getConnection();
		con.close();
	}
}
