package com.trifork.sdm.importer.importers.yderregisteret;

import java.io.File;
import java.sql.Connection;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.trifork.sdm.importer.importers.FileImporterControlledIntervals;
import com.trifork.sdm.importer.importers.FileImporterException;
import com.trifork.sdm.importer.persistence.FilePersistException;
import com.trifork.sdm.importer.persistence.mysql.MySQLConnectionManager;


public class YderregisterImporter implements FileImporterControlledIntervals {

	private static final String[] requiredFileExt = new String[] { "K05", "K40", "K45", "K1025", "K5094" };

	private final Logger logger = Logger.getLogger(getClass());


	public void importFiles(List<File> files) throws FileImporterException {

		// get the Loebenummer from the files and verify that all files have the
		// same loebenummer.
		
		String loebeNummerString = null;
		int loebeNummer;
		
		for (File f : files) {
			String curFileLoebe;

			if (f.getName().endsWith("XML") && f.getName().length() >= 15) {
				curFileLoebe = f.getName().substring(10, 15);
			}
			else {
				continue;
			}

			if (loebeNummerString == null) {
				loebeNummerString = curFileLoebe;
			}
			else {
				if (!loebeNummerString.equals(curFileLoebe)) {
					throw new FileImporterException(
							"Det blev forsøgt at importere yderregisterfiler med forskellige løbenumre. Løbenummeret fremgår af filnavnet");
				}
			}
		}

		if (loebeNummerString == null) {
			throw new FileImporterException("Der blev ikke fundet yderregister filer med et løbenummer");
		}

		loebeNummer = Integer.parseInt(loebeNummerString);

		Connection con = MySQLConnectionManager.getConnection();

		YderDao dao = new YderDao(con);

		// Verify løbenummer.
		int latestInDB = dao.getLastLoebenummer();

		if (latestInDB != 0) {
			if (latestInDB > loebeNummer) {
				throw new FilePersistException(
						"Det blev forsøgt at indlæse et yderregister med et løbenummer, der er lavere end det seneste importerede løbenummer.");
			}
		}
		
		dao.setLastLoebenummer(loebeNummer);

		logger.debug("Starting to parse yderregister");
		YderregisterParser tp = new YderregisterParser();
		YderregisterDatasets yderreg = tp.parseYderregister(files);
		logger.debug("Yderregister parsed");

		try {
			logger.debug("Starting to import yderregister into database");
			dao.persistCompleteDataset(yderreg.getYderregisterDS());
			dao.persistCompleteDataset(yderreg.getYderregisterPersonDS());
			logger.debug("Done importing yderregister into database");
			con.commit();
		}
		catch (Exception e) {
			logger.error("An error occured while persisting the yderregister to database " + e.getMessage(),
					e);
			throw new FilePersistException("An error occured while persisting the yderregister to database: "
					+ e.getMessage(), e);
		}
		finally {
			MySQLConnectionManager.close(con);
		}

	}


	public boolean areRequiredInputFilesPresent(List<File> files) {

		logger.debug("Checking yderregister file list for presence of all required files");

		Map<String, File> fileMap = new HashMap<String, File>(files.size());
		
		for (File f : files) {
			String fName = f.getName();
			if (fName.indexOf('.') != fName.lastIndexOf('.')) {
				fileMap.put(fName.substring(fName.indexOf('.') + 1, fName.lastIndexOf('.')), f);
			}
		}

		for (String reqFileExt : Arrays.asList(requiredFileExt)) {
			if (!fileMap.containsKey(reqFileExt)) {
				logger.debug("Did not find required file with extension: " + reqFileExt);
				return false;
			}
			logger.debug("Found required file: " + reqFileExt);
		}
		
		return true;
	}


	/**
	 * They should come at least each quarter
	 */
	@Override
	public Date getNextImportExpectedBefore(Date lastImport) {

		Calendar cal;

		if (lastImport == null)
			cal = Calendar.getInstance();
		else {
			cal = new GregorianCalendar();
			cal.setTime(lastImport);
		}
		
		cal.add(Calendar.DATE, 95);
		
		return cal.getTime();
	}
}
