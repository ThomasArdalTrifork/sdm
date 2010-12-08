package com.trifork.sdm.models.autorisationsregisteret;

import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import com.trifork.sdm.models.AbstractEntity;


@Entity
public class Autorisation extends AbstractEntity {
	private String nummer;
	private String cpr;

	private String fornavn;
	private String efternavn;

	private String uddKode;

	private Calendar validFrom;


	@Id
	@Column
	public String getAutorisationsnummer() {

		return nummer;
	}


	@Column
	public String getCpr() {

		return cpr;
	}


	@Column
	public String getEfternavn() {

		return efternavn;
	}


	@Column
	public String getFornavn() {

		return fornavn;
	}


	@Column
	public String getUddannelsesKode() {

		return uddKode;
	}


	@Override
	public Calendar getValidFrom() {

		return validFrom;
	}


	@Override
	public Calendar getValidTo() {

		return FUTURE;
	}


	public void setNummer(String nummer) {

		this.nummer = nummer;
	}


	public void setCpr(String cpr) {

		this.cpr = cpr;
	}


	public void setFornavn(String fornavn) {

		this.fornavn = fornavn;
	}


	public void setEfternavn(String efternavn) {

		this.efternavn = efternavn;
	}


	public void setUddKode(String uddKode) {

		this.uddKode = uddKode;
	}


	public void setValidFrom(Calendar validFrom) {

		this.validFrom = validFrom;
	}
}
