package com.trifork.sdm.models.takst;

import com.trifork.sdm.persistence.annotations.Id;
import com.trifork.sdm.persistence.annotations.Output;

@Output(name = "LaegemiddelDoseringRef")
public class Doseringskode extends TakstEntity
{

	private Long drugid; // Ref. t. LMS01
	private Long doseringskode; // Ref. t. LMS28


	@Id
	@Output
	public String getCID()
	{
		return drugid + "-" + doseringskode;
	}


	@Output(name = "DrugID")
	public long getDrugid()
	{
		return this.drugid;
	}


	public void setDrugid(Long drugid)
	{
		this.drugid = drugid;
	}


	@Output(name = "DoseringKode")
	public long getDoseringskode()
	{
		return this.doseringskode;
	}


	public void setDoseringskode(Long doseringskode)
	{
		this.doseringskode = doseringskode;
	}

}