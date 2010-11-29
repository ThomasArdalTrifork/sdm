package com.trifork.sdm.schema.models.validB;

import java.util.Date;

import com.trifork.sdm.persistence.annotations.Output;

@Output
(
	supportedVersions = {1, 3}
)
public class TestB
{
	@Output(supportedVersions = {1})
	public boolean getA()
	{
		return true;
	}
	
	@Output(supportedVersions = {1, 2})
	public long getB()
	{
		return 12345;
	}
	
	@Output(supportedVersions = {3})
	public String getC()
	{
		return "Test";
	}
	
	@Output(supportedVersions={2})
	public Date getD()
	{
		return new Date();
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
