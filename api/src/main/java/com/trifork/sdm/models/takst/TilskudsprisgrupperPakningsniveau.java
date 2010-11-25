package com.trifork.sdm.models.takst;

public class TilskudsprisgrupperPakningsniveau extends TakstEntity
{

	private Long tilskudsprisGruppe;
	private Long varenummer; // Ref. t. LMS02


	public Long getTilskudsprisGruppe()
	{
		return this.tilskudsprisGruppe;
	}


	public void setTilskudsprisGruppe(Long tilskudsprisGruppe)
	{
		this.tilskudsprisGruppe = tilskudsprisGruppe;
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