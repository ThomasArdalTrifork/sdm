package com.trifork.sdm.importer.spoolers;

import it.sauronsoftware.cron4j.Scheduler;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.log4j.Logger;

import com.trifork.sdm.importer.configuration.Configuration;


/**
 * FileSpooler. Initiates and monitor file spoolers.
 * 
 * @author Jan Buchholdt
 * 
 */
public class SpoolerManager
{
	private static final Logger logger = Logger.getLogger(SpoolerManager.class);
	
	Map<String, FileSpooler> spoolers = new HashMap<String, FileSpooler>();
	Map<String, JobSpooler> jobSpoolers = new HashMap<String, JobSpooler>();

	private static final int POLLING_INTERVAL = Configuration.getInt("inputfile.polling.interval");
	private Timer timer = new Timer(true);
	private List<JobSpooler> jobQueue = Collections.synchronizedList(new LinkedList<JobSpooler>());
	private Scheduler jobScheduler = new Scheduler();


	public SpoolerManager(String rootDir)
	{		
		String spoolerSetup = Configuration.getString("filespoolers");
		
		logger.info("File spoolers: " + spoolerSetup);
		logger.info("Root directory: " + rootDir);
		
		if (spoolerSetup.length() == 0)
		{
			logger.error("Manager created but no spooler configured. Please configure a spooler");
			
			return;
		}
		
		for (String spoolerName : spoolerSetup.split(","))
		{
			spoolers.put(spoolerName, new FileSpooler(new FileSpoolerSetup(spoolerName, rootDir)));
		}

		String jobSpoolerSetup = Configuration.getString("jobspoolers");
		
		logger.debug("Job spoolers: " + jobSpoolerSetup);

		for (String jobSpoolerName : jobSpoolerSetup.split(","))
		{
			JobSpooler jobSpooler = new JobSpooler(new JobSpoolerSetup(jobSpoolerName));
			
			jobSpoolers.put(jobSpoolerName, jobSpooler);
			jobScheduler.schedule(jobSpooler.getSetup().getSchedule(),
					new GernericJobSpoolerExecutor(jobSpooler));
		}

		jobScheduler.start();
		
		TimerTask pollTask = new PollingTask();
		timer.schedule(pollTask, 10 * 1000, POLLING_INTERVAL * 1000);
	}


	public void destroy()
	{
		timer.cancel();
		jobScheduler.stop();
	}


	/**
	 * Checks that all configured spoolers exist and are running
	 */
	public boolean isAllSpoolersRunning()
	{
		for (FileSpooler spooler : spoolers.values())
		{
			if (!spooler.getStatus().equals(FileSpooler.Status.RUNNING)) return false;
		}
		for (JobSpooler spooler : jobSpoolers.values())
		{
			if (!spooler.getStatus().equals(JobSpooler.Status.RUNNING)) return false;
		}
		return true;
	}


	/**
	 * @param uriString
	 * @return the uri converted to a file path. null if the uri was not a file
	 *         uri
	 */
	public static String uri2filepath(String uriString)
	{
		URI uri;
		try
		{
			uri = new URI(uriString);
			if (!"file".equals(uri.getScheme()))
			{
				String errorMessage = "uri2filepath(" + uriString
						+ ") can only convert uri with scheme: 'file'!";
				logger.error(errorMessage);
				return null;
			}
			return uri.getPath();
		}
		catch (URISyntaxException e)
		{
			String errorMessage = "uri2filepath must be called with a uri";
			logger.error(errorMessage);
			return null;
		}
	}


	public boolean isAllRejectedDirsEmpty()
	{
		for (FileSpooler spooler : spoolers.values())
		{
			if (!spooler.isRejectedDirEmpty())
			{
				return false;
			}
		}
		return true;
	}


	public boolean isRejectDirEmpty(String type)
	{
		FileSpooler spooler = spoolers.get(type);
		if (spooler == null) return false;
		return spooler.isRejectedDirEmpty();
	}


	public boolean isNoOverdueImports()
	{
		for (FileSpooler spooler : spoolers.values())
		{
			if (spooler.isOverdue())
			{
				return false;
			}
		}
		return true;
	}


	public Map<String, FileSpooler> getSpoolers()
	{
		return spoolers;
	}


	public FileSpooler getSpooler(String type)
	{
		return spoolers.get(type);
	}


	public Map<String, JobSpooler> getJobSpoolers()
	{
		return jobSpoolers;
	}


	public JobSpooler getJobSpooler(String type)
	{
		return jobSpoolers.get(type);
	}


	public class PollingTask extends TimerTask
	{
		Throwable t;


		public void run()
		{
			ExecutePendingJobs();
			for (FileSpooler spooler : spoolers.values())
			{
				try
				{
					spooler.execute();
				}
				catch (Throwable t)
				{
					if (this.t == null || !t.getMessage().equals(this.t.getMessage()))
					{
						logger.debug(
								"Caught throwable while polling. Only logging once to avoid log file spamming",
								t);
						this.t = t;
					}
				}
				ExecutePendingJobs();
			}
		}

	}


	private void ExecutePendingJobs()
	{
		Throwable lastt = null; // TODO: Huh? lastt? What is this?

		while (!jobQueue.isEmpty())
		{
			JobSpooler next = jobQueue.get(0);
			
			try
			{
				next.execute();
				jobQueue.remove(0);
			}
			catch (Throwable t)
			{
				if (lastt == null || !t.getMessage().equals(lastt.getMessage()))
				{
					logger.debug("Caught throwable while running job " + next.getName()
							+ ". Only logging once to avoid log file spamming", t);
					lastt = t;
				}
			}
		}
	}


	public class GernericJobSpoolerExecutor implements Runnable
	{
		final private JobSpooler jobImpl;


		public GernericJobSpoolerExecutor(JobSpooler jobImpl)
		{
			super();
			this.jobImpl = jobImpl;
		}


		public void run()
		{
			// The job is activated. Add it to the jobQueue.
			// The JobQueue is emptied in the polling task to avoid two jobs
			// running simultaneously
			jobQueue.add(jobImpl);
		}

	}

}
