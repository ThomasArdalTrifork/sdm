package com.trifork.sdm.importer.importers.sks;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.util.List;

import org.junit.Test;

import com.trifork.sdm.importer.TestHelper;
import com.trifork.sdm.models.sks.Organisation;
import com.trifork.sdm.persistence.Dataset;
import com.trifork.sdm.util.DateUtils;


public class SksParserTest {

	public static File SHAKCompleate = TestHelper.getFile("testdata/sks/SHAKCOMPLETE.TXT");


	@Test
	public void testParseSHAKCompleate() throws Throwable {

		Dataset<Organisation> org = SksParser.parseOrganisationer(SHAKCompleate);
		assertEquals(9717, org.getEntities().size());
		List<Organisation> afd600714X = org.getEntitiesById("600714X");
		assertEquals(2, afd600714X.size()); // two versions of this id exist
		assertEquals(DateUtils.toCalendar(2008, 10, 1), afd600714X.get(0).getValidFrom());
		assertEquals(DateUtils.toCalendar(2008, 11, 30), afd600714X.get(0).getValidTo());
		assertEquals("Ortopædkirurgisk skadeklinik, Middelfart", afd600714X.get(0).getNavn());

		assertEquals(DateUtils.toCalendar(2008, 12, 1), afd600714X.get(1).getValidFrom());
		assertEquals(DateUtils.toCalendar(2500, 1, 1), afd600714X.get(1).getValidTo());
		assertEquals("Skadeklinik, Middelfart", afd600714X.get(1).getNavn());

		List<Organisation> sgh4212 = org.getEntitiesById("4212");
		assertEquals(2, sgh4212.size()); // two versions of this id exist
		assertEquals(DateUtils.toCalendar(1999, 01, 1), sgh4212.get(0).getValidFrom());
		assertEquals(DateUtils.toCalendar(2008, 11, 30), sgh4212.get(0).getValidTo());
		assertEquals("Sygehus Fyn", sgh4212.get(0).getNavn());

		assertEquals(DateUtils.toCalendar(2008, 12, 1), sgh4212.get(1).getValidFrom());
		assertEquals(DateUtils.toCalendar(2500, 1, 1), sgh4212.get(1).getValidTo());
		assertEquals("OUH Svendborg Sygehus", sgh4212.get(1).getNavn());
	}

}
