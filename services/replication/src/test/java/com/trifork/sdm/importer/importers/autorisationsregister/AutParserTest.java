package com.trifork.sdm.importer.importers.autorisationsregister;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;

import org.junit.Test;

import com.trifork.sdm.importer.importers.autorisationsregisteret.AutorisationsregisterParser;
import com.trifork.sdm.importer.importers.autorisationsregisteret.Autorisationsregisterudtraek;
import com.trifork.sdm.models.autorisationsregisteret.Autorisation;

public class AutParserTest {
	public static File valid = new File("./src/test/resources/testdata/aut/valid/20090915AutDK.csv");
	public static File invalid = new File("./src/test/resources/testdata/aut/invalid/20090915AutDK.csv");

	@Test
	public void testParse() throws IOException{
		AutorisationsregisterParser parser = new AutorisationsregisterParser();
		Autorisationsregisterudtraek auts = parser.parse(valid, Calendar.getInstance());
		assertEquals(4, auts.getEntities().size());
		Autorisation a = auts.getEntityById("0013H");
		assertNotNull(a);
		assertEquals("0101280063", a.getCpr());
		assertEquals("Tage Søgaard", a.getFornavn());
	}

	@Test
	public void testInvalid() throws IOException{
		try {
			AutorisationsregisterParser parser = new AutorisationsregisterParser();
			Autorisationsregisterudtraek auts = parser.parse(invalid, Calendar.getInstance());
			auts.getEntities();
			fail();
		} catch (Exception e) {
		}
	}

}
