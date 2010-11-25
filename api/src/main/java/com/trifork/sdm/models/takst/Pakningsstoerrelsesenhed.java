package com.trifork.sdm.models.takst;

import com.trifork.sdm.persistence.annotations.Id;
import com.trifork.sdm.persistence.annotations.Output;

@Output
public class Pakningsstoerrelsesenhed extends TakstEntity
{

	private final DivEnheder enheder;


	public Pakningsstoerrelsesenhed(DivEnheder enheder)
	{
		this.enheder = enheder;
	}


	@Id
	@Output
	public String getPakningsstoerrelsesenhedKode()
	{
		return enheder.getKode();
	}


	@Output
	public String getPakningsstoerrelsesenhedTekst()
	{
		return enheder.getTekst();
	}


	@Override
	public String getEntityId()
	{
		return enheder.getKode();
	}

}
