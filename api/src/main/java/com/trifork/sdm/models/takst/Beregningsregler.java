package com.trifork.sdm.models.takst;

import com.trifork.sdm.persistence.annotations.Output;

public class Beregningsregler extends TakstEntity
{
	private String kode; // Ref. t. LMS02, felt 21
	private String tekst;


	@Output
	public String getKode()
	{
		return this.kode;
	}


	public void setKode(String kode)
	{
		this.kode = kode;
	}


	@Output
	public String getTekst()
	{
		return this.tekst;
	}


	public void setTekst(String tekst)
	{
		this.tekst = tekst;
	}


	public String getEntityId()
	{
		return "" + this.kode;
	}

}