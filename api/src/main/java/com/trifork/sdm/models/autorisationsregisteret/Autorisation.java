package com.trifork.sdm.models.autorisationsregisteret;

import java.util.Calendar;

import javax.xml.bind.annotation.XmlElement;

import com.trifork.sdm.models.AbstractEntity;
import com.trifork.sdm.persistence.annotations.Id;
import com.trifork.sdm.persistence.annotations.Output;

@Output
public class Autorisation extends AbstractEntity {
	private String nummer;
	private String cpr;

	private String fornavn;
	private String efternavn;

	private String uddKode;

	private Calendar validFrom;

	@Id
	@Output
	public String getAutorisationsnummer() {
		return nummer;
	}

	@Output
	@XmlElement
	public String getCpr() {
		return cpr;
	}

	@Output
	public String getEfternavn() {
		return efternavn;
	}

	@Output
	public String getFornavn() {
		return fornavn;
	}

	@Output
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
