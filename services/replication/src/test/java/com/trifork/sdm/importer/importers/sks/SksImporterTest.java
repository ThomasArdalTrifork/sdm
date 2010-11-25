package com.trifork.sdm.importer.importers.sks;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.ArrayList;

import org.junit.Test;

import com.trifork.sdm.importer.TestHelper;


public class SksImporterTest {

	public static File SHAKComplete = TestHelper.getFile("testdata/sks/SHAKCOMPLETE.TXT");

	// This field does not use the TestHelper because the file does not exist,
	// and the test helper would fail.
	public static File wrong = new File("testdata/sks/SHAKCOMPLETE.XML");


	@Test
	public void testAreRequiredInputFilesPresent() {

		SksImporter importer = new SksImporter();

		ArrayList<File> files = new ArrayList<File>();

		assertFalse(importer.areRequiredInputFilesPresent(files));
		files.add(SHAKComplete);
		assertTrue(importer.areRequiredInputFilesPresent(files));

		files.remove(SHAKComplete);
		files.add(wrong);
		assertFalse(importer.areRequiredInputFilesPresent(files));
	}


}
