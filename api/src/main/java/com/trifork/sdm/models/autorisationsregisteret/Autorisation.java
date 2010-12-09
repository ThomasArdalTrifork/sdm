package com.trifork.sdm.models.autorisationsregisteret;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import com.trifork.sdm.models.AbstractRecord;
import com.trifork.sdm.util.DateUtils;


@Entity
public class Autorisation extends AbstractRecord {
	
	private String nummer;
	private String cpr;

	private String fornavn;
	private String efternavn;

	private String uddKode;

	private Date validFrom;


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
	public Date getValidFrom() {

		return validFrom;
	}


	@Override
	public Date getValidTo() {

		return DateUtils.FUTURE;
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


	public void setValidFrom(Date validFrom) {

		this.validFrom = validFrom;
	}
}
