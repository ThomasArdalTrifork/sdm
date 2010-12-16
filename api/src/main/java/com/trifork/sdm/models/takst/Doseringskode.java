package com.trifork.sdm.models.takst;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.trifork.sdm.models.AbstractRecord;


@Entity
@Table(name = "LaegemiddelDoseringRef")
public class Doseringskode extends AbstractRecord {
	
	private Long drugid; // Ref. t. LMS01
	private Long doseringskode; // Ref. t. LMS28


	@Id
	@Column
	public String getCID() {

		return drugid + "-" + doseringskode;
	}


	@Column(name = "DrugID")
	public long getDrugid() {

		return this.drugid;
	}


	public void setDrugid(Long drugid) {

		this.drugid = drugid;
	}


	@Column(name = "DoseringKode")
	public long getDoseringskode() {

		return this.doseringskode;
	}


	public void setDoseringskode(Long doseringskode) {

		this.doseringskode = doseringskode;
	}

}