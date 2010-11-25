package com.trifork.sdm.models.takst;

import com.trifork.sdm.models.Entity;
import com.trifork.sdm.persistence.annotations.Output;

public class NumeriskMedEnhed extends TakstEntity implements Entity
{
	private String klartekst;
	private double numerisk;
	private Object enhed;


	public NumeriskMedEnhed(Takst takst, String klartekst, double numerisk, Object enhed)
	{
		this.takst = takst;
		this.klartekst = klartekst;
		this.numerisk = numerisk;
		this.enhed = enhed;
	}


	public String getEntityId()
	{
		return null;
	}


	@Output(name = "StyrkeTekst")
	public String getKlartekst()
	{
		return klartekst;
	}


	@Output(name = "StyrkeNumerisk")
	public double getNumerisk()
	{
		return numerisk;
	}


	@Output(name = "StyrkeEnhed")
	public String getEnhed()
	{
		if (enhed instanceof DivEnheder) return ((DivEnheder) enhed).getTekst();
		return null;
	}


	/**
	 * Only used when enhed is a String
	 */
	@Output(name = "StyrkeEnhed")
	public String getEnhedString()
	{
		if (enhed instanceof String) return (String) enhed;
		return null;
	}


	public String getEntityTypeDisplayName()
	{
		// Should probably never be used as objects of this class are always
		// nested
		return getClass().getSimpleName();
	}

}
