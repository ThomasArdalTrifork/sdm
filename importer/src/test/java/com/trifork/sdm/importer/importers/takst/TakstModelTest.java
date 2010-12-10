package com.trifork.sdm.importer.importers.takst;

import java.util.ArrayList;
import java.util.Date;

import junit.framework.TestCase;

import org.junit.Test;

import com.trifork.sdm.models.takst.ATCKoderOgTekst;
import com.trifork.sdm.models.takst.Takst;
import com.trifork.sdm.models.takst.TakstDataset;
import com.trifork.sdm.util.DateUtils;


public class TakstModelTest extends TestCase {

	@Test
	public void testManyToMany() throws Exception {

		Date from = DateUtils.toDate(2000, 1, 1);
		Date to = DateUtils.toDate(2000, 15, 1);
		
		Takst takst = new Takst(from, to);
		
		TakstDataset<ATCKoderOgTekst> atckoder = new TakstDataset<ATCKoderOgTekst>(takst,
				new ArrayList<ATCKoderOgTekst>(), ATCKoderOgTekst.class);
		takst.addDataset(atckoder);
	}
}
