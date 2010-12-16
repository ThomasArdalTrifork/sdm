package com.trifork.sdm.models.takst.unused;

import com.trifork.sdm.models.AbstractRecord;

public class EmballagetypeKoder extends AbstractRecord {
	
	private String kode; // Ref. t. LMS02, felt 09
	private String kortTekst;
	private String tekst;


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