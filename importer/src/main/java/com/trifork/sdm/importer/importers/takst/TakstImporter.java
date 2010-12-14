package com.trifork.sdm.importer.importers.takst;

import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.log4j.Logger;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.trifork.sdm.importer.importers.FileImporterControlledIntervals;
import com.trifork.sdm.importer.importers.FileImporterException;
import com.trifork.sdm.importer.persistence.FilePersistException;
import com.trifork.sdm.importer.persistence.RecordDao;
import com.trifork.sdm.importer.persistence.mysql.MySQLConnectionManager;
import com.trifork.sdm.importer.persistence.mysql.MySQLTemporalDao;
import com.trifork.sdm.models.takst.Takst;


public class TakstImporter implements FileImporterControlledIntervals {
	private final Logger logger = Logger.getLogger(getClass());

	public static final String[] requiredFileNames = new String[] { "system.txt", "lms01.txt", "lms02.txt",
			"lms03.txt", "lms04.txt", "lms05.txt", "lms07.txt", "lms09.txt", "lms10.txt", "lms11.txt",
			"lms12.txt", "lms13.txt", "lms14.txt", "lms15.txt", "lms16.txt", "lms17.txt", "lms18.txt",
			"lms19.txt", "lms20.txt", "lms23.txt", "lms24.txt", "lms25.txt", "lms26.txt", "lms27.txt",
			"lms28.txt" };

	// final String[] optionalFileNames = new String[] {"lms32.txt",
	// "lms21.txt",
	// "lms22.txt","lms29.txt","lms30.txt","lms31.txt"};

	private static final DateTimeFormatter weekFormatter = DateTimeFormat.forPattern("xxxxww").withLocale(
			new Locale("da", "DK"));


	public void importFiles(List<File> files) throws FileImporterException {

		Takst takst;
		logger.debug("Starting to parse takst");
		TakstParser tp = new TakstParser();
		takst = tp.parseTakst(files);
		logger.debug("Takst parsed");
		Connection con = null;
		try {
			logger.debug("Starting to import takst into database");
			con = MySQLConnectionManager.getConnection();
			RecordDao versionedDao = new MySQLTemporalDao(con);
			versionedDao.persistCompleteDatasets(takst.getDatasets());
			logger.debug("Done importing takst into database");
			try {
				con.commit();
			}
			catch (SQLException e) {
				throw new FileImporterException("could not commit transaction", e);
			}

		}
		catch (Exception e) {
			logger.error("An error occured while persisting the takst to database " + e.getMessage(), e);
			throw new FilePersistException("An error occured while persisting the takst to database: "
					+ e.getMessage(), e);
		}
		finally {
			MySQLConnectionManager.close(con);
		}
	}


	public boolean areRequiredInputFilesPresent(List<File> files) {

		logger.debug("Checking takst file list for presence of all required files");
		Map<String, File> fileMap = new HashMap<String, File>(files.size());
		for (File f : files)
			fileMap.put(f.getName(), f);

		for (String reqFile : Arrays.asList(requiredFileNames)) {
			if (!fileMap.containsKey(reqFile)) {
				logger.debug("Did not find required file: " + reqFile);
				return false;
			}
			logger.debug("Found required file: " + reqFile);
		}
		return true;
	}


	/**
	 * Der findes to typer takster: Ordinære takster og "indimellem" takster.
	 * Ordinære takster skal komme hver 14. dag.
	 * 
	 * "Indimellem" takster kommer ad-hoc, og vi kan ikke sætte forventning op
	 * til dem.
	 */
	public Date getNextImportExpectedBefore(Date lastImport) {

		Connection connection = null;
		Statement statement = null;
		Calendar ordinaryTakst = null;

		try {
			connection = MySQLConnectionManager.getAutoCommitConnection();

			// Check to see if we have any 'takst' at all.

			statement = connection.createStatement();
			ResultSet countResult = statement.executeQuery("SELECT COUNT(*) AS RowCount FROM TakstVersion");
			countResult.next();
			int count = countResult.getInt("RowCount");
			countResult.close();
			statement.close();

			if (count != 0) {
				statement = connection.createStatement();

				ResultSet rs = statement.executeQuery("SELECT MAX(TakstUge) FROM TakstVersion");

				if (rs.next()) {
					
					String lastWeek = rs.getString(1);

					logger.info(lastWeek);

					ordinaryTakst = weekFormatter.parseDateTime(lastWeek).toGregorianCalendar();

					// Next ordinary 'takst' expected 14 days after.
					ordinaryTakst.add(Calendar.DATE, 14);

					// We want the ordinary 'takst' to be imported 36 hours
					// before it is suppose to be in effect.
					ordinaryTakst.add(Calendar.HOUR, -36);
				}
			}
		}
		catch (Exception e) {
			
			logger.error("Cannot get latest TakstVersion from database.", e);
		}
		finally {
			
			MySQLConnectionManager.close(statement, connection);
		}

		if (ordinaryTakst == null) {
			// Something failed. Raise an alarm by setting the expected next
			// import to past time.
			// FIXME (thb): Should this not be done more explicitly?

			ordinaryTakst = Calendar.getInstance();
			ordinaryTakst.add(Calendar.HOUR, -1);
		}

		return ordinaryTakst.getTime();
	}

}
