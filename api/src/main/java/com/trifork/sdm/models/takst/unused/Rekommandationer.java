package com.trifork.sdm.models.takst.unused;

import com.trifork.sdm.models.AbstractRecord;


public class Rekommandationer extends AbstractRecord {

	private Long rekommandationsgruppe;

	private Long drugID; // Ref. t. LMS01
	private Long varenummer; // Ref. t. LMS02

	// Værdier: Anbefales / Anbefales med forbehold / Anbefales ikke
	private String rekommandationsniveau;


	public Long getRekommandationsgruppe() {

		return this.rekommandationsgruppe;
	}


	public void setRekommandationsgruppe(Long rekommandationsgruppe) {

		this.rekommandationsgruppe = rekommandationsgruppe;
	}


	public Long getDrugID() {

		return this.drugID;
	}


	public void setDrugID(Long drugID) {

		this.drugID = drugID;
	}


	public Long getVarenummer() {

		return this.varenummer;
	}


	public void setVarenummer(Long varenummer) {

		this.varenummer = varenummer;
	}


	public String getRekommandationsniveau() {

		return this.rekommandationsniveau;
	}


	public void setRekommandationsniveau(String rekommandationsniveau) {

		this.rekommandationsniveau = rekommandationsniveau;
	}


	@Override
	public Long getKey() {

		return varenummer;
	}

}