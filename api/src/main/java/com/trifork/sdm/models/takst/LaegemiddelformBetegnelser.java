package com.trifork.sdm.models.takst;

import com.trifork.sdm.persistence.annotations.Id;
import com.trifork.sdm.persistence.annotations.Output;

@Output(name = "Formbetegnelse")
public class LaegemiddelformBetegnelser extends TakstEntity
{

	private String kode; // Ref. t. LMS01, felt 08
	private String tekst;
	private String aktivInaktiv; // A (Aktiv)=DLS o.l.-I (inaktiv)=Ikke
									// anerkendt term


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
	public String getTekst()
	{
		return this.tekst;
	}


	public void setTekst(String tekst)
	{
		this.tekst = tekst;
	}


	public Boolean getAktivInaktiv()
	{
		return "A".equalsIgnoreCase(this.aktivInaktiv);
	}


	public void setAktivInaktiv(String aktivInaktiv)
	{
		this.aktivInaktiv = aktivInaktiv;
	}


	public String getEntityId()
	{
		return "" + this.kode;
	}

}