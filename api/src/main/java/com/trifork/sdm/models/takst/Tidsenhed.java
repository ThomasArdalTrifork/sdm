package com.trifork.sdm.models.takst;

import com.trifork.sdm.persistence.annotations.Id;
import com.trifork.sdm.persistence.annotations.Output;

@Output
public class Tidsenhed extends TakstEntity
{

	private final DivEnheder enheder;


	public Tidsenhed(DivEnheder enheder)
	{
		this.enheder = enheder;
	}


	@Id
	@Output
	public String getTidsenhedKode()
	{
		return enheder.getKode();
	}


	@Output
	public String getTidsenhedTekst()
	{
		return enheder.getTekst();
	}


	@Override
	public String getEntityId()
	{
		return enheder.getKode();
	}

}
