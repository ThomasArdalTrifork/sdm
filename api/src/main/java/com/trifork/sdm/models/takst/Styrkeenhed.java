package com.trifork.sdm.models.takst;

import com.trifork.sdm.persistence.annotations.Id;
import com.trifork.sdm.persistence.annotations.Output;

@Output
public class Styrkeenhed extends TakstEntity
{

	private final DivEnheder enheder;


	public Styrkeenhed(DivEnheder enheder)
	{
		this.enheder = enheder;
	}


	@Id
	@Output
	public String getStyrkeenhedKode()
	{
		return enheder.getKode();
	}


	@Output
	public String getStyrkeenhedTekst()
	{
		return enheder.getTekst();
	}


	@Override
	public String getEntityId()
	{
		return enheder.getKode();
	}

}