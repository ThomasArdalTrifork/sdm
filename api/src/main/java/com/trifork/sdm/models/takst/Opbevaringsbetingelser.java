package com.trifork.sdm.models.takst;

public class Opbevaringsbetingelser extends TakstEntity {

	private String kode; // Ref. t. LMS02, felt 17
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


	public String getEntityId() {

		return "" + this.kode;
	}

}