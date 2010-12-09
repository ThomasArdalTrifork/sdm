package com.trifork.sdm.importer.importers.autorisationsregisteret;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.apache.log4j.Logger;

import com.trifork.sdm.importer.importers.FileImporterControlledIntervals;
import com.trifork.sdm.importer.importers.FileImporterException;
import com.trifork.sdm.importer.persistence.mysql.MySQLConnectionManager;
import com.trifork.sdm.importer.persistence.mysql.MySQLTemporalDao;


public class AutImporter implements FileImporterControlledIntervals {
	
	private Logger logger = Logger.getLogger(getClass());


	public boolean areRequiredInputFilesPresent(List<File> files) {

		if (files.size() == 0) return false;
		for (File file : files) {
			if (getDateFromInputFileName(file.getName()) == null) return false;
		}
		return true;
	}


	public void importFiles(List<File> files) throws FileImporterException {

		Connection connection = null;
		try {
			connection = MySQLConnectionManager.getConnection();
			MySQLTemporalDao dao = new MySQLTemporalDao(connection);

			doImport(files, dao);

			connection.commit();
		}
		catch (SQLException e) {
			String message = "Error using database during import of autorisationsregister";
			logger.error(message, e);

			throw new FileImporterException(message, e);
		}
		finally {
			MySQLConnectionManager.close(connection);
		}
	}


	/**
	 * Import Autorisationsregister-files using the Dao
	 * 
	 * @param files
	 *            , the files from which the Autorisationer should be parsed
	 * @param dao
	 *            , the dao to which Autorisationer should be saved
	 * @throws SQLException
	 *             If something goes wrong in the DAO
	 * @throws FileImporterException
	 *             If importing fails
	 */
	public void doImport(List<File> files, MySQLTemporalDao dao) throws FileImporterException {

		for (File file : files) {

			Date date = getDateFromInputFileName(file.getName());

			if (date == null) {
				
				throw new FileImporterException("Filename format is invalid! Date could not be extracted");
			}
				
			try {

				AutorisationsregisterParser parser = new AutorisationsregisterParser();
				Autorisationsregisterudtraek dataset = parser.parse(file, date);
				dao.persistCompleteDataset(dataset);

			}
			catch (Exception e) {
				
				String mess = "Error reader autorisationsfil: " + file;
				logger.error(mess, e);
				throw new FileImporterException(mess, e);
			}
		}
	}


	/**
	 * Extracts the date from the filename
	 * 
	 * @param fileName
	 * @return
	 */
	public Date getDateFromInputFileName(String fileName) {

		try {
			int year = new Integer(fileName.substring(0, 4));
			int month = new Integer(fileName.substring(4, 6));
			int date = new Integer(fileName.substring(6, 8));

			return new GregorianCalendar(year, month - 1, date).getTime();
		}
		catch (NumberFormatException e) {

			return null;
		}
	}


	/**
	 * Largest gap observed was 15 days from 2008-10-18 to 2008-11-01
	 */
	public Date getNextImportExpectedBefore(Date lastImport) {

		Calendar calendar;

		if (lastImport == null)
			calendar = Calendar.getInstance();
		else {
			calendar = new GregorianCalendar();
			calendar.setTime(lastImport);
		}

		calendar.add(Calendar.MONTH, 1);

		return calendar.getTime();
	}

}
