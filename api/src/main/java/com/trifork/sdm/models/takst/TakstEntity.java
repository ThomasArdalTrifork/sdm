package com.trifork.sdm.models.takst;

import java.util.Calendar;

import javax.xml.bind.annotation.XmlType;

import com.trifork.sdm.models.AbstractEntity;


@XmlType(name = "takst")
public abstract class TakstEntity extends AbstractEntity
{
	protected Takst takst;
	
	
	@Override
	public Calendar getValidFrom()
	{
		return takst.getValidFrom();
	}
}
