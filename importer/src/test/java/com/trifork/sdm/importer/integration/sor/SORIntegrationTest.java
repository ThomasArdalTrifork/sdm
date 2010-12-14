package com.trifork.sdm.importer.integration.sor;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.trifork.sdm.importer.TestHelper;
import com.trifork.sdm.importer.importers.sor.SORImporter;
import com.trifork.sdm.importer.persistence.mysql.MySQLConnectionManager;


public class SORIntegrationTest {

	public static File onePraksis = TestHelper.getFile("testdata/sor/ONE_PRAKSIS.xml");
	public static File oneSygehus = TestHelper.getFile("testdata/sor/ONE_SYGEHUS.xml");
	public static File oneApotek = TestHelper.getFile("testdata/sor/ONE_APOTEK.xml");
	public static File fullSor = TestHelper.getFile("testdata/sor/SOR_FULL.xml");


	@After
	@Before
	public void cleanDb() throws Throwable {

		Connection connection = MySQLConnectionManager.getAutoCommitConnection();
		
		Statement stm = connection.createStatement();
		stm.executeQuery("truncate table Praksis");
		stm.executeQuery("truncate table Yder");
		stm.executeQuery("truncate table Sygehus");
		stm.executeQuery("truncate table SygehusAfdeling");
		stm.executeQuery("truncate table Apotek");
		stm.close();
		
		connection.close();
	}


	@Test
	public void testImport() throws Exception {

		SORImporter importer = new SORImporter();
		ArrayList<File> files = new ArrayList<File>();

		files.add(fullSor);
		importer.importFiles(files);

		Connection con = MySQLConnectionManager.getAutoCommitConnection();
		Statement stmt = con.createStatement();
		ResultSet rs = stmt.executeQuery("Select count(*) from Praksis");
		rs.next();

		assertEquals(3148, rs.getInt(1));
		rs.close();

		rs = stmt.executeQuery("Select count(*) from Yder");
		rs.next();
		assertEquals(5434, rs.getInt(1));
		rs.close();

		rs = stmt.executeQuery("Select count(*) from Sygehus");
		rs.next();
		assertEquals(469, rs.getInt(1));
		rs.close();

		rs = stmt.executeQuery("Select count(*) from SygehusAfdeling");
		rs.next();
		assertEquals(2890, rs.getInt(1));
		rs.close();

		rs = stmt.executeQuery("Select count(*) from Apotek");
		rs.next();
		// assertEquals(1, rs.getInt(1));
		assertEquals(328, rs.getInt(1));
		rs.close();

		rs = stmt.executeQuery("Select count(*) from Praksis where ValidTo < now()");
		rs.next();
		assertEquals(49, rs.getInt(1));
		rs.close();

		rs = stmt.executeQuery("Select count(*) from Yder where ValidTo < now()");
		rs.next();
		assertEquals(451, rs.getInt(1));
		rs.close();

		rs = stmt.executeQuery("Select count(*) from Sygehus where ValidTo < now()");
		rs.next();
		assertEquals(20, rs.getInt(1));
		rs.close();

		rs = stmt.executeQuery("Select count(*) from SygehusAfdeling where ValidTo < now()");
		rs.next();
		assertEquals(255, rs.getInt(1));
		rs.close();

		rs = stmt.executeQuery("Select count(*) from Apotek where ValidTo < now()");
		rs.next();
		assertEquals(2, rs.getInt(1));
		rs.close();

		rs = stmt.executeQuery("Select count(*) from Praksis where ValidTo > now()");
		rs.next();
		assertEquals(3148 - 49, rs.getInt(1));
		rs.close();

		rs = stmt.executeQuery("Select count(*) from Yder where ValidTo > now()");
		rs.next();
		assertEquals(5434 - 451, rs.getInt(1));
		rs.close();

		rs = stmt.executeQuery("Select count(*) from Sygehus where ValidTo > now()");
		rs.next();
		assertEquals(469 - 20, rs.getInt(1));
		rs.close();

		rs = stmt.executeQuery("Select count(*) from SygehusAfdeling where ValidTo > now()");
		rs.next();
		assertEquals(2890 - 255, rs.getInt(1));
		rs.close();

		rs = stmt.executeQuery("Select count(*) from Apotek where ValidTo > now()");
		rs.next();
		assertEquals(328 - 2, rs.getInt(1));
		rs.close();
		stmt.close();
		con.close();
	}
}
