package com.trifork.sdm.models.sor;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import com.trifork.sdm.models.AbstractRecord;
import com.trifork.sdm.util.DateUtils;


@Entity
public class Praksis extends AbstractRecord {
	private Date validFrom;
	private String navn;
	private Long eanLokationsnummer;
	private Long regionCode;
	private Long sorNummer;
	private Date validTo;


	public Praksis() {

	}


	@Column
	public String getNavn() {

		return navn;
	}


	public void setNavn(String navn) {

		this.navn = navn;
	}


	@Column
	public Long getEanLokationsnummer() {

		return eanLokationsnummer;
	}


	public void setEanLokationsnummer(Long eanLokationsnummer) {

		this.eanLokationsnummer = eanLokationsnummer;
	}


	@Column
	public Long getRegionCode() {

		return regionCode;
	}


	public void setRegionCode(Long regionCode) {

		this.regionCode = regionCode;
	}


	@Id
	@Column
	public Long getSorNummer() {

		return sorNummer;
	}


	public void setSorNummer(Long sorNummer) {

		this.sorNummer = sorNummer;
	}


	public Date getValidFrom() {

		return validFrom;
	}


	public void setValidFrom(Date validFrom) {

		this.validFrom = validFrom;
	}


	@Override
	public Date getValidTo() {

		return (validTo != null) ? validTo : DateUtils.FUTURE;
	}


	public void setValidTo(Date validTo) {

		this.validTo = validTo;
	}
}
