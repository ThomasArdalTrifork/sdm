package com.trifork.sdm.models.takst;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;


@Entity
public class Medicintilskud extends TakstRecord {

	private String kode; // Ref. t. LMS02, felt 12
	private String kortTekst;
	private String tekst;


	@Id
	@Column
	public String getKode() {

		return this.kode;
	}


	public void setKode(String kode) {

		this.kode = kode;
	}


	@Column
	public String getKortTekst() {

		return this.kortTekst;
	}


	public void setKortTekst(String kortTekst) {

		this.kortTekst = kortTekst;
	}


	@Column
	public String getTekst() {

		return this.tekst;
	}


	public void setTekst(String tekst) {

		this.tekst = tekst;
	}


	@Override
	public String getKey() {

		return "" + this.kode;
	}

}