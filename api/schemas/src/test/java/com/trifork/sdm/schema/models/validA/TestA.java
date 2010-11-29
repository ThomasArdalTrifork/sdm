package com.trifork.sdm.schema.models.validA;

import java.util.Date;

import com.trifork.sdm.persistence.annotations.Output;


@Output
public class TestA
{
	/**
	 * This property is placed at the top because we need to test that the
	 * order of the elements in the generated schema is alphabetical.
	 * 
	 * DO NOT MOVE.
	 */
	@Output
	public boolean getB()
	{
		return true;
	}
	
	
	@Output
	public String getA()
	{
		return "FAKE";
	}
	

	@Output
	public Date getC()
	{
		return new Date();
	}


	@Output
	public int getD()
	{
		return 1234;
	}


	@Output
	public long getE()
	{
		return 1234l;
	}


	/**
	 * This element should not be output, because supported versions is by
	 * default {1}.
	 */
	@Output(supportedVersions = { 2 })
	public int getF()
	{
		return 1234;
	}


	/**
	 * This element should not be output because it is not annotated with
	 * @Output.
	 */
	public int getG()
	{
		return 1234;
	}
	
	public Date getValidTo()
	{
		return null;
	}
	
	public Date getValidFrom()
	{
		return null;
	}
	
	public Date getCreatedDate()
	{
		return null;
	}
	
	public Date getModifiedDate()
	{
		return null;	
	}
}
