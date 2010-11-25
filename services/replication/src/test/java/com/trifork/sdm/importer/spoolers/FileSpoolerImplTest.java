package com.trifork.sdm.importer.spoolers;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.*;

import java.io.File;
import java.io.FileWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.NameFileFilter;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.trifork.sdm.importer.importers.FileImporterException;
import com.trifork.sdm.importer.importers.ImportTimeManager;
import com.trifork.sdm.importer.persistence.mysql.MySQLConnectionManager;


/**
 * SingleFileSpoolerImplTest. Tests that single files are spooled correctly.
 * 
 * @author Jan Buchholdt
 */

public class FileSpoolerImplTest
{
	private String spoolerDir = System.getProperty("java.io.tmpdir") + "/FileSpoolerImplTest";
	private FileSpooler spooler;


	@Before
	public void makeImple()
	{
		deleteFile(new File(spoolerDir));
		spooler = new FileSpooler(new FileSpoolerSetup("TestSpooler", spoolerDir,
			FakeFileImporter.class));

		try
		{
			Connection con = MySQLConnectionManager.getAutoCommitConnection();
			con.createStatement().executeUpdate(
				"truncate table " + MySQLConnectionManager.getHousekeepingDBName() + ".Import");
		}
		catch (SQLException e)
		{
			// NOTE: To change body of catch statement use File | Settings |
			// File Templates.

			e.printStackTrace();
		}
	}


	@After
	public void cleanUpTest() throws Exception
	{
		deleteFile(new File(spoolerDir));
	}


	@Test
	public void testConstructImpl()
	{
		assertNotNull(spooler);

		assertTrue(spooler.getInputDir().isDirectory());
		assertTrue(spooler.getInputDir().canWrite());
		assertTrue(spooler.getProcessingDir().isDirectory());
		assertTrue(spooler.getProcessingDir().canWrite());
		assertTrue(spooler.getRejectedDir().isDirectory());
		assertTrue(spooler.getRejectedDir().canWrite());
	}


	@Test
	public void testMoveProcessingFilesBackToInput() throws Exception
	{
		// Setup: Create a file in the processing directory.

		File processingSubdir = new File(spooler.getProcessingDir() + "/" + "xxxyyyzzz/");
		File processingFile = new File(processingSubdir.getAbsolutePath() + "/fil.txt");
		File inputFile = new File(spooler.getInputDir() + "/fil.txt");

		processingSubdir.mkdirs();
		processingFile.createNewFile();

		assertTrue(processingSubdir.exists());
		assertTrue(processingFile.exists());
		assertFalse(inputFile.exists());

		// It should now be moved back to input directory.
		
		spooler.moveProcessingFilesBackToInput();

		// Check that the file is gone in processing dir and present in input
		// directory.
		
		assertFalse(processingFile.exists());
		assertFalse(processingSubdir.exists());
		assertTrue(inputFile.exists());
	}


	@Test
	public void testGetDirSignature() throws Exception
	{
		// Setup 1: Create an empty dir
		File dir = new File(spoolerDir + "/dir1");
		assertTrue(dir.mkdirs());
		long s = FileSpooler.getDirSignature(dir);

		// Check that same signature is returned
		assertEquals(s, FileSpooler.getDirSignature(dir));

		// Setup 2: add a file
		File file = new File(dir.getAbsolutePath() + "/file");
		assertTrue(file.createNewFile());
		// Check that a new signature is returned
		assertFalse(s == FileSpooler.getDirSignature(dir));
		s = FileSpooler.getDirSignature(dir);
		// Check that same signature is returned
		assertEquals(s, FileSpooler.getDirSignature(dir));

		// Setup 3: Write a char to the file
		FileWriter fw = new FileWriter(file);
		fw.write('x');
		fw.close();
		// Check that a new signature is returned
		assertFalse(s == FileSpooler.getDirSignature(dir));
		s = FileSpooler.getDirSignature(dir);
		// Check that same signature is returned
		assertEquals(s, FileSpooler.getDirSignature(dir));
	}


	@Test
	public void testIsRejectedDirsEmpty() throws Exception
	{
		assertTrue(spooler.isRejectedDirEmpty());
		File f = new File(spooler.getRejectedDir().getAbsolutePath() + "/file");
		f.createNewFile();
		assertFalse(spooler.isRejectedDirEmpty());
	}


	@Test
	public void testPollNoFiles() throws Exception
	{
		spooler.execute();
		assertEquals(spooler.getStatus(), FileSpooler.Status.RUNNING);
		assertEquals(spooler.getActivity(), FileSpooler.Activity.AWAITING);
		assertNull(spooler.getMessage());
	}


	@Test
	public void testPollInputFile() throws Exception
	{
		File f = new File(spooler.getInputDir() + "/f");
		assertTrue(f.createNewFile());
		spooler.execute();
		assertEquals(spooler.getStatus(), FileSpooler.Status.RUNNING);
		assertEquals(spooler.getActivity(), FileSpooler.Activity.STABILIZING);
		assertNull(spooler.getMessage());
	}


	// @Test
	public void testImportSucess() throws Exception
	{
		// Create an input file
		File f = new File(spooler.getInputDir() + "/f");
		assertTrue(f.createNewFile());
		// "Cheat" and make it look as if the file is stable now, so we dont
		// have to wait
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MILLISECOND, -1000);
		spooler.stabilizationPeriodEnd = cal;
		spooler.inputdirSignature = FileSpooler.getDirSignature(spooler.getInputDir());
		
		// Make an importer that always succeeds
		
		FakeFileImporter succeedingImporter = new FakeFileImporter()
		{
			@Override
			public boolean areRequiredInputFilesPresent(List<File> files)
			{
				return true;
			}
		};
		
		spooler.importer = succeedingImporter;
		Calendar beforeCall = Calendar.getInstance();

		// Polling now should trigger import
		spooler.execute();

		// Check that status/activity is set correctly after import
		assertEquals(spooler.getStatus(), FileSpooler.Status.RUNNING);
		assertEquals(spooler.getActivity(), FileSpooler.Activity.AWAITING);
		// Check that no error message is set
		assertNull(spooler.getMessage());
		// Check that the importer was called once
		assertEquals(1, succeedingImporter.importFileCalled);

		// Check that importtime was set in mysql and that we can get it out
		Calendar importTime = ImportTimeManager.getLastImportTime(spooler.getSetup().getName());
		assertNotNull(importTime);
		// Check that importtime was set to the timestamp of the execution. I.e.
		// before now
		assertTrue(importTime.before(Calendar.getInstance()));
		// Check that importtime was set to the timestamp of the execution. I.e.
		// after before the call.
		// Due to MySQL not having sub-second presicion and the fact that it
		// rounds down, a second is added before the comparision
		importTime.add(1, Calendar.SECOND);
		assertTrue(importTime.after(beforeCall));

		// Check that the input files are deleted, as they should be after
		// succesful processing
		assertTrue(spooler.getInputDir().listFiles().length == 0);
		assertTrue(spooler.getProcessingDir().listFiles().length == 0);
		assertTrue(spooler.getRejectedDir().listFiles().length == 0);
		assertFalse(f.exists());
	}


	@Test
	public void testImportFailure() throws Exception
	{
		// create a file
		File f = new File(spooler.getInputDir() + "/f");
		assertTrue(f.createNewFile());
		// "Cheat" and make it look as if it is stable
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MILLISECOND, -1000);
		spooler.stabilizationPeriodEnd = cal;
		spooler.inputdirSignature = FileSpooler.getDirSignature(spooler.getInputDir());

		// Make an importer that always fails
		FakeFileImporter importer = new FakeFileImporter()
		{
			@Override
			public boolean areRequiredInputFilesPresent(List<File> files)
			{
				return true;
			}


			@Override
			public void importFiles(List<File> files) throws FileImporterException
			{
				throw new FileImporterException("errormsg");
			}
		};
		spooler.importer = importer;

		// Do import and check that the failure is handled correctly
		spooler.execute();
		// there should be created a new dir in rejected with the input file and
		// a RejectReason file
		assertEquals(
			2,
			FileUtils.listFiles(spooler.getRejectedDir(), TrueFileFilter.INSTANCE,
				TrueFileFilter.INSTANCE).size());
		assertEquals(
			1,
			FileUtils.listFiles(spooler.getRejectedDir(), new NameFileFilter("f"),
				TrueFileFilter.INSTANCE).size());
		// and a rejectreason
		File rejReason = (File) FileUtils
			.listFiles(spooler.getRejectedDir(), new NameFileFilter("RejectReason"),
				TrueFileFilter.INSTANCE).iterator().next();
		assertTrue(FileUtils.readFileToString(rejReason).contains("errormsg"));
		assertFalse(f.exists());
		assertEquals(FileSpooler.Status.ERROR, spooler.getStatus());
		assertEquals("errormsg", spooler.getMessage());
		assertNull(ImportTimeManager.getLastImportTime(spooler.getSetup().getName()));
		// No files should be present in input or processing dirs
		assertEquals(
			0,
			FileUtils.listFiles(spooler.getInputDir(), TrueFileFilter.INSTANCE,
				TrueFileFilter.INSTANCE).size());
		assertEquals(
			0,
			FileUtils.listFiles(spooler.getProcessingDir(), TrueFileFilter.INSTANCE,
				TrueFileFilter.INSTANCE).size());
	}


	/**
	 * This function will recursively delete directories and files.
	 * 
	 * @param path
	 *            File or Directory to be deleted
	 * @return true indicates success.
	 */
	public static boolean deleteFile(File path)
	{
		if (path.exists())
		{
			if (path.isDirectory())
			{
				File[] files = path.listFiles();
				for (int i = 0; i < files.length; i++)
				{
					if (files[i].isDirectory())
					{
						deleteFile(files[i]);
					}
					else
					{
						files[i].delete();
					}
				}
			}
		}
		return (path.delete());
	}
}
