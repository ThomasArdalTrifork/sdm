package com.trifork.sdm.models.takst;

import com.trifork.sdm.util.DateUtils;

public class UdgaaedeNavne extends TakstEntity
{
	private Long drugid; // Ref. t. LMS01
	private Long datoForAendringen;
	private String tidligereNavn;


	public Long getDrugid()
	{
		return this.drugid;
	}


	public void setDrugid(Long drugid)
	{
		this.drugid = drugid;
	}


	public String getDatoForAendringen()
	{
		return DateUtils.toISO8601date(this.datoForAendringen);
	}


	public void setDatoForAendringen(Long datoForAendringen)
	{
		this.datoForAendringen = datoForAendringen;
	}


	public String getTidligereNavn()
	{
		return this.tidligereNavn;
	}


	public void setTidligereNavn(String tidligereNavn)
	{
		this.tidligereNavn = tidligereNavn;
	}


	public String getEntityId()
	{
		return datoForAendringen + '-' + tidligereNavn + '-' + drugid;
	}

}