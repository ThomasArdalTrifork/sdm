package com.trifork.sdm.schema.models.invalidE;

import java.util.Date;

import com.trifork.sdm.persistence.annotations.Output;

@Output
public class TestE
{
	public String getA()
	{
		return null;
		
	}
	
	// The error is that validTo is missing. This property is required.
	
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
