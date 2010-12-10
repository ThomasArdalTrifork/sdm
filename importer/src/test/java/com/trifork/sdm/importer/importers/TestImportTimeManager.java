package com.trifork.sdm.importer.importers;

import org.junit.Test;

import java.util.Calendar;
import java.util.Date;

import static org.junit.Assert.assertEquals;


public class TestImportTimeManager {

	private final String spooler = "testSpooler";


	@Test
	public void test() {

		Calendar now = Calendar.getInstance();
		now.set(Calendar.MILLISECOND, 0);

		ImportTimeManager.setImportTime(spooler, now.getTime());

		Date time1 = now.getTime();
		Date time2 = ImportTimeManager.getLastImportTime(spooler);

		assertEquals(time1, time2);
	}
}
