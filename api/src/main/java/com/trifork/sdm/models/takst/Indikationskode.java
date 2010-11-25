package com.trifork.sdm.models.takst;

import com.trifork.sdm.persistence.annotations.Id;
import com.trifork.sdm.persistence.annotations.Output;

@Output(name = "IndikationATCRef")
public class Indikationskode extends TakstEntity
{

	private String aTC; // Ref. t. LMS01
	private Long indikationskode; // Ref. t. LMS26
	private Long drugID; // Ref. t. LMS01, felt 01


	@Id
	@Output
	public String getCID()
	{
		// TODO: Get rid of this ugly calculated ID. Should be handled by the
		// DAO
		// A calculated ID. Necessary because the DAO implementation needs a
		// single key
		return aTC + "-" + indikationskode;
	}


	@Output
	public String getATC()
	{
		return this.aTC;
	}


	public void setATC(String aTC)
	{
		this.aTC = aTC;
	}


	@Output(name = "IndikationKode")
	public Long getIndikationskode()
	{
		return this.indikationskode;
	}


	public void setIndikationskode(Long indikationskode)
	{
		this.indikationskode = indikationskode;
	}


	public Long getDrugID()
	{
		return this.drugID;
	}


	public void setDrugID(Long drugID)
	{
		this.drugID = drugID;
	}

}