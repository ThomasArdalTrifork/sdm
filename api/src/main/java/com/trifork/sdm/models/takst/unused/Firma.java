package com.trifork.sdm.models.takst.unused;

import javax.persistence.Column;

import com.trifork.sdm.models.AbstractRecord;


public class Firma extends AbstractRecord {
	
	private Long firmanummer; // Ref. t. LMS01, felt 13 - 14
	private String firmamaerkeKort; // TODO: P.t. tomt

	private String firmamaerkeLangtNavn;
	private String parallelimportoerKode;


	@Column
	public Long getFirmanummer() {

		return this.firmanummer;
	}


	public void setFirmanummer(Long firmanummer) {

		this.firmanummer = firmanummer;
	}


	@Column
	public String getFirmamaerkeKort() {

		return this.firmamaerkeKort;
	}


	public void setFirmamaerkeKort(String firmamaerkeKort) {

		this.firmamaerkeKort = firmamaerkeKort;
	}


	@Column
	public String getFirmamaerkeLangtNavn() {

		return this.firmamaerkeLangtNavn;
	}


	public void setFirmamaerkeLangtNavn(String firmamaerkeLangtNavn) {

		this.firmamaerkeLangtNavn = firmamaerkeLangtNavn;
	}


	@Column
	public String getParallelimportoerKode() {

		return this.parallelimportoerKode;
	}


	public void setParallelimportoerKode(String parallelimportoerKode) {

		this.parallelimportoerKode = parallelimportoerKode;
	}


	@Override
	public Long getKey() {

		return this.firmanummer;
	}

}