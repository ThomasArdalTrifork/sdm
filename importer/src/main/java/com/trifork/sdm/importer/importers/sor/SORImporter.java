package com.trifork.sdm.importer.importers.sor;

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


public class SORImporter implements FileImporterControlledIntervals {
	private static final Logger logger = Logger.getLogger(SORImporter.class);


	public boolean areRequiredInputFilesPresent(List<File> files) {

		if (files.size() == 0) return false;
		boolean xmlpresent = false;
		for (File file : files) {
			if (file.getName().endsWith(".xml")) xmlpresent = true;
		}
		return xmlpresent;
	}


	public void importFiles(List<File> files) throws FileImporterException {

		Connection connection = null;
		try {
			connection = MySQLConnectionManager.getConnection();
			MySQLTemporalDao dao = new MySQLTemporalDao(connection);
			for (File file : files) {
				SORDataSets dataSets = SORParser.parse(file);
				dao.persistCompleteDataset(dataSets.getPraksisDS());
				dao.persistCompleteDataset(dataSets.getYderDS());
				dao.persistCompleteDataset(dataSets.getSygehusDS());
				dao.persistCompleteDataset(dataSets.getSygehusAfdelingDS());
				dao.persistCompleteDataset(dataSets.getApotekDS());
			}
			connection.commit();
		}
		catch (SQLException e) {
			try {
				connection.rollback();
			}
			catch (SQLException e1) {
				logger.error("Cannot rollback", e1);
			}
			String mess = "Error using database during import of autorisationsregister";
			logger.error(mess, e);
			throw new FileImporterException(mess, e);
		}
		finally {
			MySQLConnectionManager.close(connection);
		}
	}


	/**
	 * Should be updated every day
	 */
	public Date getNextImportExpectedBefore(Date lastImport) {

		Calendar cal;

		if (lastImport == null)
			cal = Calendar.getInstance();
		else {
			cal = new GregorianCalendar();
			cal.setTime(lastImport);
		}

		cal.add(Calendar.DATE, 3);

		return cal.getTime();
	}

}
