package com.trifork.sdm.importer.spoolers;

import java.util.Calendar;
import java.util.Date;

import org.apache.log4j.Logger;

import com.trifork.sdm.importer.jobs.Job;
import com.trifork.sdm.util.DateUtils;


public class JobSpooler extends BasicSpooler
{
	private static Logger logger = Logger.getLogger(FileSpooler.class);

	private final JobSpoolerSetup setup;
	private Job job;

	Calendar lastRun = null;


	public JobSpooler(JobSpoolerSetup setup)
	{
		super();

		this.setup = setup;

		try
		{
			job = setup.getJobExecutorClass().newInstance();
		}
		catch (Exception e)
		{
			logger.error("Could not instantiate importer of class", e);
			setMessage("Spooler cannot get an instance if importer class. Please change the setup");
			setStatus(Status.ERROR);
			
			return;
		}

		setStatus(Status.RUNNING);
		setActivity(Activity.AWAITING);
	}


	public JobSpoolerSetup getSetup()
	{
		return setup;
	}


	public String getLastRunFormatted()
	{
		if (lastRun == null)
			return "Never";
		else
			return DateUtils.toMySQLdate(lastRun);
	}


	public Calendar getLastRun()
	{
		return lastRun;
	}


	@Override
	public String getName()
	{
		return setup.getName();
	}


	@Override
	public void execute()
	{
		if (getStatus() == Status.RUNNING)
		{
			try
			{
				setActivity(Activity.EXECUTING);
				
				job.run();
				
				Calendar runTime = Calendar.getInstance();
				runTime.setTime(new Date());
				lastRun = runTime;
				
				setActivity(Activity.AWAITING);
			}
			catch (Exception e)
			{
				String message = "Job " + getName() + " failed during job execution.";
				
				logger.error(message, e);
				
				setMessage(message);
				
				setStatus(Status.ERROR);
			}
		}
	}
}
