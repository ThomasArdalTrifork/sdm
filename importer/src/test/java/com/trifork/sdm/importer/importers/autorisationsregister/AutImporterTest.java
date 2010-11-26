package com.trifork.sdm.importer.importers.autorisationsregister;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.trifork.sdm.importer.TestHelper;
import com.trifork.sdm.importer.importers.autorisationsregisteret.AutImporter;
import com.trifork.sdm.importer.importers.autorisationsregisteret.AutorisationsregisterParser;
import com.trifork.sdm.importer.persistence.mysql.MySQLTemporalDao;
import com.trifork.sdm.persistence.CompleteDataset;


public class AutImporterTest {

	public static File valid = TestHelper.getFile("testdata/aut/valid/20090915AutDK.csv");
	private AutImporter importer;


	@Before
	public void Setup() {

		importer = new AutImporter();
	}


	@Test
	public void testAreRequiredInputFilesPresent() throws IOException {

		List<File> files = new ArrayList<File>();

		// empty set

		assertFalse(importer.areRequiredInputFilesPresent(files));
		files.add(new File("blabla.nowayamigo"));

		// wrong file name and empty file.

		assertFalse(importer.areRequiredInputFilesPresent(files));
		files.add(valid);

		// one bad and one good file.

		assertFalse(importer.areRequiredInputFilesPresent(files));
		files = new ArrayList<File>();
		files.add(valid);

		// one good file

		assertTrue(importer.areRequiredInputFilesPresent(files));
		files.add(valid);

		// two good files

		assertTrue(importer.areRequiredInputFilesPresent(files));
	}


	AutorisationsregisterParser parser = new AutorisationsregisterParser();


	@Test
	public void testImport() throws Exception {

		List<File> files = new ArrayList<File>();
		files.add(valid);
		MySQLTemporalDao daoMock = mock(MySQLTemporalDao.class);
		importer.doImport(files, daoMock);
		verify(daoMock).persistCompleteDataset(any(CompleteDataset.class));
	}


	@Test
	public void testGetDateFromFileName() {

		AutImporter importer = new AutImporter();
		Date date = importer.getDateFromInputFileName("19761110sgfdgfg").getTime();
		assertEquals("19761110", new SimpleDateFormat("yyyyMMdd").format(date));
	}

}
