package com.trifork.sdm.models.sor;

import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import com.trifork.sdm.models.AbstractEntity;


@Entity
public class Apotek extends AbstractEntity {

	private Long sorNummer;
	private Long apotekNummer;
	private Long filialNummer;
	private Long eanLokationsnummer;
	private Long cvr;
	private Long pcvr;
	private String navn;
	private String telefon;
	private String vejnavn;
	private String postnummer;
	private String bynavn;
	private String email;
	private String www;
	private Calendar validFrom;
	private Calendar validTo;


	public Apotek() {

	}


	@Id
	@Column
	public Long getSorNummer() {

		return sorNummer;
	}


	public void setSorNummer(Long sorNummer) {

		this.sorNummer = sorNummer;
	}


	@Column
	public Long getApotekNummer() {

		return apotekNummer;
	}


	public void setApotekNummer(Long apotekNummer) {

		this.apotekNummer = apotekNummer;
	}


	@Column
	public Long getFilialNummer() {

		return filialNummer;
	}


	public void setFilialNummer(Long filialNummer) {

		this.filialNummer = filialNummer;
	}


	@Column
	public Long getEanLokationsnummer() {

		return eanLokationsnummer;
	}


	public void setEanLokationsnummer(Long eanLokationsnummer) {

		this.eanLokationsnummer = eanLokationsnummer;
	}


	@Column
	public Long getCvr() {

		return cvr;
	}


	public void setCvr(Long cvr) {

		this.cvr = cvr;
	}


	@Column
	public Long getPcvr() {

		return pcvr;
	}


	public void setPcvr(Long pcvr) {

		this.pcvr = pcvr;
	}


	@Column
	public String getNavn() {

		return navn;
	}


	public void setNavn(String navn) {

		this.navn = navn;
	}


	@Column
	public String getTelefon() {

		return telefon;
	}


	public void setTelefon(String telefon) {

		this.telefon = telefon;
	}


	@Column
	public String getVejnavn() {

		return vejnavn;
	}


	public void setVejnavn(String vejnavn) {

		this.vejnavn = vejnavn;
	}


	@Column
	public String getPostnummer() {

		return postnummer;
	}


	public void setPostnummer(String postnummer) {

		this.postnummer = postnummer;
	}


	@Column
	public String getBynavn() {

		return bynavn;
	}


	public void setBynavn(String bynavn) {

		this.bynavn = bynavn;
	}


	@Column
	public String getEmail() {

		return email;
	}


	public void setEmail(String email) {

		this.email = email;
	}


	@Column
	public String getWww() {

		return www;
	}


	public void setWww(String www) {

		this.www = www;
	}


	public Calendar getValidFrom() {

		return validFrom;
	}


	public void setValidFrom(Calendar validFrom) {

		this.validFrom = validFrom;
	}


	public Calendar getValidTo() {

		return (validTo != null) ? validTo : FUTURE;
	}


	public void setValidTo(Calendar validTo) {

		this.validTo = validTo;
	}


	public Calendar getCreatedDate() {

		return new GregorianCalendar();
	}
}
