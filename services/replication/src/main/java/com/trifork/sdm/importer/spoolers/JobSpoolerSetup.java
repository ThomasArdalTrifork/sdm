package com.trifork.sdm.importer.spoolers;

import org.apache.log4j.Logger;

import com.trifork.sdm.importer.configuration.Configuration;
import com.trifork.sdm.importer.jobs.Job;


public class JobSpoolerSetup
{
	private static Logger logger = Logger.getLogger(FileSpoolerSetup.class);
	
	private final String jobName;
	private Class<? extends Job> jobClass;


	public JobSpoolerSetup(String jobName)
	{
		super();
		this.jobName = jobName;
	}


	public Class<? extends Job> getJobExecutorClass()
	{
		if (jobClass == null)
		{
			String s = getConfig("jobExecutorClass", null);
			resolveImporterClass(s);
		}
		
		return jobClass;
	}


	public String getSchedule()
	{
		return getConfig("schedule", null);
	}


	@SuppressWarnings("unchecked")
	private void resolveImporterClass(String executorName)
	{
		if (executorName == null)
		{
			logger.error("Configuration error. You need to configure a executer class for the job '"
					+ getName() + "'. Set property " + getConfigEntry("jobExecutorClass")
					+ " to the class path of the job executor");
		}
		try
		{
			Class<?> executor = Class.forName(executorName);
			jobClass = (Class<? extends Job>) executor;
		}
		catch (ClassNotFoundException e)
		{
			logger.error("Configuration error. The configured job executor class (" + executorName
					+ " could not be found. " + "Set property "
					+ getConfigEntry("jobExecutorClass") + " to a valid job executor");
		}
		catch (ClassCastException e)
		{
			logger.error("Configuration error. The configured job executor class (" + executorName
					+ " didn't implement interface " + Job.class.getName()
					+ ". Set property " + getConfigEntry("jobExecutorClass")
					+ " to a valid job executor");
		}
	}


	public String getName()
	{
		return jobName;
	}


	String getConfigEntry(String key)
	{
		return "jobspooler." + getName() + "." + key;
	}


	String getConfig(String key, String Default)
	{
		String value = Configuration.getString(getConfigEntry(key));
		if (value != null) return value;
		
		return Default;
	}

}
