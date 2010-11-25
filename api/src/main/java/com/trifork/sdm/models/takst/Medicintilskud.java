package com.trifork.sdm.models.takst;

import com.trifork.sdm.persistence.annotations.Id;
import com.trifork.sdm.persistence.annotations.Output;

@Output
public class Medicintilskud extends TakstEntity
{

	private String kode; // Ref. t. LMS02, felt 12
	private String kortTekst;
	private String tekst;


	@Id
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
	public String getKortTekst()
	{
		return this.kortTekst;
	}


	public void setKortTekst(String kortTekst)
	{
		this.kortTekst = kortTekst;
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