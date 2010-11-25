package com.trifork.sdm.importer.configuration;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Logger;


public class Configuration
{
	private static Logger logger = Logger.getLogger(Configuration.class.getName());

	private static Configuration defaultInstance = new Configuration();

	private List<Properties> properties = new ArrayList<Properties>();


	private void loadPropertyFile(String path, Map<String, InputStream> fileList)
	{
		InputStream in = getClass().getResourceAsStream(path);

		if (in != null)
		{
			fileList.put(path, in);
		}
		else
		{
			logger.warning(String.format("Could not find property file '%s'.", path));
		}
	}


	public Configuration()
	{
		Map<String, InputStream> propFiles = new HashMap<String, InputStream>();

		loadPropertyFile("/config.properties", propFiles);
		loadPropertyFile("/spooler.config.properties", propFiles);

		for (Map.Entry<String, InputStream> propFile : propFiles.entrySet())
		{
			logger.info("Property file '" + propFile.getKey() + "':");

			Properties properties = new Properties();

			try
			{
				properties.load(propFile.getValue());

				this.properties.add(properties);
			}
			catch (IOException e)
			{
				logger.warning(propFile.getKey() + " not found or read");
			}

			// Print out the properties.

			for (String propertyKey : properties.stringPropertyNames())
			{
				String value;

				// Mask the passwords.

				if (propertyKey.indexOf("pwd") > -1 || propertyKey.indexOf("pasword") > -1)
				{
					value = "********";
				}
				else
				{
					value = getProperty(propertyKey);
				}

				logger.info(propertyKey + " = " + value);
			}
		}
	}


	public Configuration(String file)
	{
		try
		{
			Properties properties = new Properties();
			properties.load(new FileInputStream("/" + file));

			this.properties.add(properties);
		}
		catch (Exception e)
		{
			throw new RuntimeException(e);
		}
	}


	public String getNotNullProperty(String key)
	{
		for (Properties properties : this.properties)
		{
			String result = properties.getProperty(key);

			if (result != null && result.length() > 0) return result;
		}

		throw new RuntimeException("No value found for property key: " + key);
	}


	public int getIntProperty(String key)
	{
		return Integer.parseInt(getNotNullProperty(key));
	}


	public Date getDateProperty(String key)
	{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try
		{
			return sdf.parse(getNotNullProperty(key));
		}
		catch (ParseException e)
		{
			throw new RuntimeException(e);
		}
	}


	public static String getString(String key)
	{
		return defaultInstance.getProperty(key);
	}


	public static Integer getInt(String key)
	{
		return Integer.parseInt(defaultInstance.getProperty(key));
	}


	private String getProperty(String key)
	{
		for (Properties properties : this.properties)
		{
			String result = properties.getProperty(key);
			if (result != null) return result;
		}

		return null;
	}


	public static void setDefaultInstance(Configuration conf)
	{
		// Only for unit tests
		defaultInstance = conf;
	}
}
