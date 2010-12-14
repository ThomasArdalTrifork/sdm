package com.trifork.sdm.importer.importers.sks;

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
import com.trifork.sdm.importer.persistence.RecordDao;
import com.trifork.sdm.importer.persistence.mysql.MySQLConnectionManager;
import com.trifork.sdm.importer.persistence.mysql.MySQLTemporalDao;
import com.trifork.sdm.models.sks.Organisation;
import com.trifork.sdm.persistence.Dataset;


public class SksImporter implements FileImporterControlledIntervals
{
	private static final Logger logger = Logger.getLogger(com.trifork.sdm.importer.importers.FileImporterControlledIntervals.class);


	/*
	 * SKS files usually arrive monthly
	 */
	public Date getNextImportExpectedBefore(Date lastImport)
	{
		Calendar cal;

		if (lastImport == null)
			cal = Calendar.getInstance();
		else {
			cal = new GregorianCalendar();
			cal.setTime(lastImport);
		}
		
		cal.add(Calendar.DATE, 45);
		
		return cal.getTime();
	}


	public boolean areRequiredInputFilesPresent(List<File> files)
	{
		boolean present = false;
		for (File file : files)
		{
			if (file.getName().toUpperCase().endsWith(".TXT")) present = true;
		}
		return present;
	}


	public void importFiles(List<File> files) throws FileImporterException
	{
		Connection con = null;
		try
		{
			con = MySQLConnectionManager.getConnection();
			RecordDao dao = new MySQLTemporalDao(con);
			
			for (File file : files)
			{
				if (file.getName().toUpperCase().endsWith(".TXT"))
				{
					Dataset<Organisation> dataset = SksParser.parseOrganisationer(file);
					logger.debug("Done parsing " + dataset.getEntities().size() + " from file: "
							+ file.getName());
					dao.persistDeltaDataset(dataset);
				}
				else
				{
					logger.warn("Ignoring file, which neither matches *.TXT. File: "
							+ file.getAbsolutePath());
				}
			}
			try
			{
				con.commit();
			}
			catch (SQLException e)
			{
				throw new FileImporterException("could not commit transaction", e);
			}
		}
		finally
		{
			MySQLConnectionManager.close(con);
		}
	}
}
