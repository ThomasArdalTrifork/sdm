package com.trifork.sdm.models.sor;

import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.trifork.sdm.models.AbstractRecord;
import com.trifork.sdm.models.Record;


@Entity
@Table(name = "Yder")
public class Yder extends AbstractRecord implements Record {
	private String nummer;
	private Long eanLokationsnummer;
	private String telefon;
	private String navn;
	private String vejnavn;
	private String postnummer;
	private String bynavn;
	private String email;
	private String www;
	private Long hovedSpecialeKode;
	private String hovedSpecialeTekst;
	private Long sorNummer;
	private Long praktisSorNummer;
	private Calendar validFrom;
	private Calendar validTo;


	@Column
	public String getNummer() {

		return nummer;
	}


	public void setNummer(String nummer) {

		this.nummer = nummer;
	}


	@Column
	public Long getEanLokationsnummer() {

		return eanLokationsnummer;
	}


	public void setEanLokationsnummer(Long eanLokationsnummer) {

		this.eanLokationsnummer = eanLokationsnummer;
	}


	@Column
	public String getTelefon() {

		return telefon;
	}


	public void setTelefon(String telefon) {

		this.telefon = telefon;
	}


	@Column
	public String getNavn() {

		return navn;
	}


	public void setNavn(String navn) {

		this.navn = navn;
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


	@Column
	public Long getHovedSpecialeKode() {

		return hovedSpecialeKode;
	}


	public void setHovedSpecialeKode(Long hovedSpecialeKode) {

		this.hovedSpecialeKode = hovedSpecialeKode;
	}


	@Column
	public String getHovedSpecialeTekst() {

		return hovedSpecialeTekst;
	}


	public void setHovedSpecialeTekst(String hovedSpecialeTekst) {

		this.hovedSpecialeTekst = hovedSpecialeTekst;
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
	public Long getPraksisSorNummer() {

		return praktisSorNummer;
	}


	public void setPraksisSorNummer(Long praktisSorNummer) {

		this.praktisSorNummer = praktisSorNummer;
	}


	@Override
	public Calendar getValidFrom() {

		return validFrom;
	}


	public void setValidFrom(Calendar validFrom) {

		this.validFrom = validFrom;
	}


	@Override
	public Calendar getValidTo() {

		return (validTo != null) ? validTo : FUTURE;
	}


	public void setValidTo(Calendar validTo) {

		this.validTo = validTo;
	}
}
