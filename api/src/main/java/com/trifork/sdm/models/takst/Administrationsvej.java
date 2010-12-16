package com.trifork.sdm.models.takst;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import com.trifork.sdm.models.AbstractRecord;


@Entity
public class Administrationsvej extends AbstractRecord {

	// Reference to LMS01, field 16
	private String kode;

	private String kortTekst;

	private String tekst;


	@Id
	@Column(name = "AdministrationsvejKode")
	public String getKode() {

		return kode;
	}


	public void setKode(String kode) {

		this.kode = kode;
	}


	public String getKortTekst() {

		return kortTekst;
	}


	public void setKortTekst(String kortTekst) {

		this.kortTekst = kortTekst;
	}


	@Column(name = "AdministrationsvejTekst")
	public String getTekst() {

		return tekst;
	}


	public void setTekst(String tekst) {

		this.tekst = tekst;
	}


	@Override
	public String getKey() {

		return kode.toString();
	}
}