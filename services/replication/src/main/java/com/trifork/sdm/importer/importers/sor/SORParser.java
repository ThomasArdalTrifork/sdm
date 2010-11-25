package com.trifork.sdm.importer.importers.sor;

import java.io.File;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.log4j.Logger;

import com.trifork.sdm.importer.importers.FileParseException;


public class SORParser
{
	private static Logger logger = Logger.getLogger(SORParser.class);


	public static SORDataSets parse(File file) throws FileParseException
	{
		SORDataSets dataSets = new SORDataSets();
		SOREventHandler handler = new SOREventHandler(dataSets);
		SAXParserFactory factory = SAXParserFactory.newInstance();
		try
		{
			SAXParser parser = factory.newSAXParser();

			if (file.getName().toUpperCase().endsWith("XML"))
			{
				parser.parse(file, handler);
			}
			else
			{
				logger.warn("Can only parse files with extension 'XML'! Ignoring: "
						+ file.getAbsolutePath());
			}

		}
		catch (Exception e)
		{
			String errorMessage = "Error parsing data from file: " + file.getAbsolutePath();
			logger.error(errorMessage, e);
			throw new FileParseException(errorMessage, e);
		}
		return dataSets;
	}
}
