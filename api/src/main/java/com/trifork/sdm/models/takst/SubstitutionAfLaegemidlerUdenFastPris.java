package com.trifork.sdm.models.takst;

public class SubstitutionAfLaegemidlerUdenFastPris extends TakstEntity
{

	private Long substitutionsgruppenummer; // Substitutionsgruppe for pakningen
	private Long varenummer;


	public Long getSubstitutionsgruppenummer()
	{
		return this.substitutionsgruppenummer;
	}


	public void setSubstitutionsgruppenummer(Long substitutionsgruppenummer)
	{
		this.substitutionsgruppenummer = substitutionsgruppenummer;
	}


	public Long getVarenummer()
	{
		return this.varenummer;
	}


	public void setVarenummer(Long varenummer)
	{
		this.varenummer = varenummer;
	}


	public Long getEntityId()
	{
		return varenummer;
	}

}