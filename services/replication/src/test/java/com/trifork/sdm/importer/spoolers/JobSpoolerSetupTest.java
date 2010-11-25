package com.trifork.sdm.importer.spoolers;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.trifork.sdm.importer.jobs.Job;


/**
 * FileSpoolerTest. Tests that setup of files and filesets are handled correct.
 * 
 * @author Jan Buchholdt
 */

public class JobSpoolerSetupTest implements Job
{
	@Before
	public void setupTest() throws Exception
	{
	}


	@After
	public void cleanUpTest() throws Exception
	{
	}


	@Test
	public void runSetupTest() throws Exception
	{
		JobSpoolerSetup setup = new JobSpoolerSetup("testjobspooler");
		
		Assert.assertEquals(this.getClass().getName(), setup.getJobExecutorClass().getName());
		Assert.assertEquals("* 1 * * *", setup.getSchedule());
	}


	public void run()
	{
		// Do nothing.
	}
}
