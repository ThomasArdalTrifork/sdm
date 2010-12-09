package com.trifork.sdm.models.takst;

public class Laegemiddelnavn extends TakstEntity {

	private Long drugid; // Ref. t. LMS01, felt 01
	private String laegemidletsUforkortedeNavn;


	public Long getDrugid() {

		return this.drugid;
	}


	public void setDrugid(Long drugid) {

		this.drugid = drugid;
	}


	public String getLaegemidletsUforkortedeNavn() {

		return this.laegemidletsUforkortedeNavn;
	}


	public void setLaegemidletsUforkortedeNavn(String laegemidletsUforkortedeNavn) {

		this.laegemidletsUforkortedeNavn = laegemidletsUforkortedeNavn;
	}


	public String getRecordId() {

		return "" + this.drugid;
	}

}