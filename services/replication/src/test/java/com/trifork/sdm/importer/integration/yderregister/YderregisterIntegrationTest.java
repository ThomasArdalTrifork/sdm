package com.trifork.sdm.importer.integration.yderregister;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Arrays;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.trifork.sdm.importer.importers.FileImporterException;
import com.trifork.sdm.importer.importers.yderregisteret.YderregisterImporter;
import com.trifork.sdm.importer.persistence.mysql.MySQLConnectionManager;

/*
 * Tests that an import of an yderregister can be performed correctly
 * @author Anders Bo Christensen
 * 
 */
public class YderregisterIntegrationTest {

	private static final String PROJECT_BASE_DIR = ".";
	private static final String TESTDATA_DIR = "/src/test/resources/testdata/yderregister";

	@Before
    @After
	public void cleanDatabase() throws Exception {
		Connection con = MySQLConnectionManager.getAutoCommitConnection();
		Statement stmt = con.createStatement();
		stmt.execute("truncate table Yderregister");		
		stmt.execute("truncate table YderregisterPerson");		
		stmt.execute("truncate table YderLoebenummer");
		stmt.close();
		con.close();

	}

	@Test
	public void ImportTest() throws Exception {
		// Arrange
		File fInitial = new File(PROJECT_BASE_DIR + TESTDATA_DIR + "/initial/");

		// Act and assert
		new YderregisterImporter().importFiles(Arrays.asList((fInitial.listFiles())));
		
		Connection con = MySQLConnectionManager.getAutoCommitConnection();
		Statement stmt = con.createStatement();
		
		ResultSet rs = stmt.executeQuery("SELECT count(*) FROM Yderregister;");
		rs.next();
		assertEquals("Samlet antal yderere", 12, rs.getInt(1));
		
		rs = stmt.executeQuery("SELECT * FROM Yderregister WHERE Nummer = 4219;");
		if (!rs.next())	{
			Assert.fail("Fandt ikke den forventede yder med ydernummer 4219");
		}
		
		assertEquals("Yders Navn", "Jørgen Vagn Nielsen", rs.getString("Navn"));
		assertEquals("Yders Vejnavn", "Store Kongensgade 96,4.", rs.getString("Vejnavn"));
		assertEquals("Yders Telefon", "33112299", rs.getString("Telefon"));
		assertEquals("Yders Postnummer", "1264", rs.getString("Postnummer"));
		assertEquals("Yders Bynavn", "København K" , rs.getString("Bynavn"));
		assertEquals("Yders Amtsnummer", 84, rs.getInt("AmtNummer"));
		assertEquals("Yders Email", "klinik@33112299.dk", rs.getString("Email"));
		assertEquals("Yders www", "www.plib.dk", rs.getString("Www"));
		assertEquals("Yders www", "1978-07-01 00:00:00.0", rs.getString("ValidFrom"));
		assertEquals("Yders www", "2999-12-31 00:00:00.0", rs.getString("ValidTo"));
        stmt.close();
		con.close();
	}

	
	@Test
	public void LoebenummerTest() throws Exception {
		// Arrange
		File fInitial = new File(PROJECT_BASE_DIR + TESTDATA_DIR + "/initial/");
		File fNext = new File(PROJECT_BASE_DIR + TESTDATA_DIR + "/nextversion/");

		new YderregisterImporter().importFiles(Arrays.asList((fInitial.listFiles())));			

		// Act and assert
		try { 
			new YderregisterImporter().importFiles(Arrays.asList((fNext.listFiles())));
			fail("Den forventede undtagelse blev ikke fanget");
		} catch (FileImporterException fe) {
		}

	}
	
	@Test
	public void CompleteFilesTest() throws Exception {
		File fInitial = new File(PROJECT_BASE_DIR + TESTDATA_DIR + "/initial/");
		
		YderregisterImporter yImp = new YderregisterImporter();
		boolean isComplete = yImp.areRequiredInputFilesPresent(Arrays.asList(fInitial.listFiles()));
		
		assertEquals("Expected to be complete", true , isComplete);
	}

	
	@Test
	public void IncompleteFilesTest() throws Exception {
		File fInitial = new File(PROJECT_BASE_DIR + TESTDATA_DIR + "/incomplete/");
		
		YderregisterImporter yImp = new YderregisterImporter();
		boolean isComplete = yImp.areRequiredInputFilesPresent(Arrays.asList(fInitial.listFiles()));
		
		assertEquals("Expected to be incomplete", false , isComplete);
	}
}
