package com.trifork.sdm.models.takst;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;


@Entity
public class Administrationsvej extends TakstEntity {

	private String kode; // Ref. t. LMS01, felt 16
	private String kortTekst;
	private String tekst;


	@Id
	@Column(name = "AdministrationsvejKode")
	public String getKode() {

		return this.kode;
	}


	public void setKode(String kode) {

		this.kode = kode;
	}


	public String getKortTekst() {

		return this.kortTekst;
	}


	public void setKortTekst(String kortTekst) {

		this.kortTekst = kortTekst;
	}


	@Column(name = "AdministrationsvejTekst")
	public String getTekst() {

		return this.tekst;
	}


	public void setTekst(String tekst) {

		this.tekst = tekst;
	}


	public String getEntityId() {

		return "" + this.kode;
	}

}