package com.trifork.sdm.models.takst.unused;

import javax.persistence.Column;

import com.trifork.sdm.models.AbstractRecord;


public class Beregningsregler extends AbstractRecord {

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


	@Override
	public String getKey() {

		return "" + this.kode;
	}

}