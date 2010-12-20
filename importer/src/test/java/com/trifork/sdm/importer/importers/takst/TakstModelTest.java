package com.trifork.sdm.importer.importers.takst;

import java.util.ArrayList;
import java.util.Date;

import junit.framework.TestCase;

import org.junit.Test;

import com.trifork.sdm.models.takst.ATCKoderOgTekst;
import com.trifork.sdm.models.takst.TakstRelease;
import com.trifork.sdm.models.takst.TakstRelease;
import com.trifork.sdm.util.DateUtils;


public class TakstModelTest extends TestCase {

	@Test
	public void testManyToMany() throws Exception {

		Date from = DateUtils.toDate(2000, 1, 1);
		Date to = DateUtils.toDate(2000, 15, 1);
		
		TakstRelease takst = new TakstRelease(from, to);
		
		TakstDataset<ATCKoderOgTekst> atckoder = new TakstDataset<ATCKoderOgTekst>(takst,
				new ArrayList<ATCKoderOgTekst>(), ATCKoderOgTekst.class);
		takst.addRecordSet(atckoder);
	}
}
