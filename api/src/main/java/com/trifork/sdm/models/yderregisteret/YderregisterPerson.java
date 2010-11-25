package com.trifork.sdm.models.yderregisteret;

import java.util.Calendar;
import java.util.Date;

import com.trifork.sdm.models.AbstractEntity;
import com.trifork.sdm.persistence.annotations.Id;
import com.trifork.sdm.persistence.annotations.Output;
import com.trifork.sdm.util.DateUtils;


@Output
public class YderregisterPerson extends AbstractEntity
{
	private String nummer;
	private String histIdPerson;
	private String cpr;
	private Long personrolleKode;
	private String personrolleTxt;
	private Date tilgangDato;
	private Date afgangDato;


	@Id
	@Output
	public String getId()
	{
		return nummer + "-" + cpr;
	}


	@Output
	public String getNummer()
	{
		return nummer;
	}


	public void setNummer(String nummer)
	{
		this.nummer = nummer;
	}


	@Output
	public String getHistIdPerson()
	{
		return histIdPerson;
	}


	public void setHistIdPerson(String histIdPerson)
	{
		this.histIdPerson = histIdPerson;
	}


	@Output
	public String getCpr()
	{
		return cpr;
	}


	public void setCpr(String cpr)
	{
		this.cpr = cpr;
	}


	@Output
	public Long getPersonrolleKode()
	{
		return personrolleKode;
	}


	public void setPersonrolleKode(Long personrolleKode)
	{
		this.personrolleKode = personrolleKode;
	}


	@Output
	public String getPersonrolleTxt()
	{
		return personrolleTxt;
	}


	public void setPersonrolleTxt(String personrolleTxt)
	{
		this.personrolleTxt = personrolleTxt;
	}


	public Date getTilgangDato()
	{
		return tilgangDato;
	}


	public void setTilgangDato(Date tilgangDato)
	{
		this.tilgangDato = tilgangDato;
	}


	public Date getAfgangDato()
	{
		return afgangDato;
	}


	public void setAfgangDato(Date afgangDato)
	{
		this.afgangDato = afgangDato;
	}


	@Override
	public Calendar getValidFrom()
	{
		return DateUtils.toCalendar(tilgangDato);
	}


	@Override
	public Calendar getValidTo()
	{
		if (afgangDato != null) return DateUtils.toCalendar(afgangDato);

		return FUTURE;
	}

}
