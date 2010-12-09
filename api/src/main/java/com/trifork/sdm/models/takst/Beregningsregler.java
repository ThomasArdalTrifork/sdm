package com.trifork.sdm.models.takst;

import javax.persistence.Column;


public class Beregningsregler extends TakstRecord {
	private String kode; // Ref. t. LMS02, felt 21
	private String tekst;


	@Column
	public String getKode() {

		return this.kode;
	}


	public void setKode(String kode) {

		this.kode = kode;
	}


	@Column
	public String getTekst() {

		return this.tekst;
	}


	public void setTekst(String tekst) {

		this.tekst = tekst;
	}


	public String getRecordId() {

		return "" + this.kode;
	}

}