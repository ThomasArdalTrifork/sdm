package com.trifork.sdm.importer.jobs;

public class JobException extends Exception
{
	private static final long serialVersionUID = 1L;


	public JobException(String message, Throwable cause)
	{
		super(message, cause);
	}


	public JobException(String message)
	{
		super(message);
	}
}
