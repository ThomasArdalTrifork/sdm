package com.trifork.sdm.importer.spoolers;

import org.apache.log4j.Logger;

// TODO: Extract interface.
public abstract class BasicSpooler
{
	private static Logger logger = Logger.getLogger(BasicSpooler.class);

	private Status status = Status.INITIATING;
	private Activity activity = Activity.AWAITING;
	private String message;


	public enum Activity
	{
		AWAITING, STABILIZING, IMPORTING, EXECUTING
	}


	public enum Status
	{
		INITIATING, RUNNING, ERROR
	}


	public Activity getActivity()
	{
		return activity;
	}


	void setStatus(Activity activity)
	{
		if (this.activity != activity)
		{
			this.activity = activity;
			logger.debug("Changed activity to: " + this.activity);
		}
	}


	void setStatus(Status status)
	{
		if (this.status == status) return;
		
		logger.debug("Status for spooler " + getName() + " changed from " + this.status + " to " + status);
		
		this.status = status;
		
		if (getStatus().equals(Status.ERROR))
		{
			logger.error("Spooler " + getName() + " error message: " + getMessage());
		}
	}


	void setMessage(String message)
	{
		this.message = message;
	}


	public String getMessage()
	{
		return message;
	}


	public Status getStatus()
	{
		return status;
	}


	abstract public void execute();


	abstract public String getName();
}
