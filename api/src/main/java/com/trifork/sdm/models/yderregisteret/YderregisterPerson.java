package com.trifork.sdm.models.yderregisteret;

import java.util.Calendar;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import com.trifork.sdm.models.AbstractRecord;
import com.trifork.sdm.util.DateUtils;


@Entity
public class YderregisterPerson extends AbstractRecord {
	private String nummer;
	private String histIdPerson;
	private String cpr;
	private Long personrolleKode;
	private String personrolleTxt;
	private Date tilgangDato;
	private Date afgangDato;


	@Id
	@Column
	public String getId() {

		return nummer + "-" + cpr;
	}


	@Column
	public String getNummer() {

		return nummer;
	}


	public void setNummer(String nummer) {

		this.nummer = nummer;
	}


	@Column
	public String getHistIdPerson() {

		return histIdPerson;
	}


	public void setHistIdPerson(String histIdPerson) {

		this.histIdPerson = histIdPerson;
	}


	@Column
	public String getCpr() {

		return cpr;
	}


	public void setCpr(String cpr) {

		this.cpr = cpr;
	}


	@Column
	public Long getPersonrolleKode() {

		return personrolleKode;
	}


	public void setPersonrolleKode(Long personrolleKode) {

		this.personrolleKode = personrolleKode;
	}


	@Column
	public String getPersonrolleTxt() {

		return personrolleTxt;
	}


	public void setPersonrolleTxt(String personrolleTxt) {

		this.personrolleTxt = personrolleTxt;
	}


	public Date getTilgangDato() {

		return tilgangDato;
	}


	public void setTilgangDato(Date tilgangDato) {

		this.tilgangDato = tilgangDato;
	}


	public Date getAfgangDato() {

		return afgangDato;
	}


	public void setAfgangDato(Date afgangDato) {

		this.afgangDato = afgangDato;
	}


	@Override
	public Calendar getValidFrom() {

		return DateUtils.toCalendar(tilgangDato);
	}


	@Override
	public Calendar getValidTo() {

		if (afgangDato != null) return DateUtils.toCalendar(afgangDato);

		return FUTURE;
	}

}
