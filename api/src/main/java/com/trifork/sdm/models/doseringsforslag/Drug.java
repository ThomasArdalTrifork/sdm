package com.trifork.sdm.models.doseringsforslag;

import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import com.trifork.sdm.Documented;
import com.trifork.sdm.models.AbstractEntity;


@Entity
@Documented("Indeholder information om lægemidlers drug-id og doseringsenhed.")
public class Drug extends AbstractEntity {

	private Calendar validFrom;
	private int releaseNumber;
	private long drugId;
	private int dosageUnitCode;


	// Don't output this.
	// @Column(length=15)
	public int getReleaseNumber() {

		return releaseNumber;
	}


	public void setReleaseNumber(int releaseNumber) {

		this.releaseNumber = releaseNumber;
	}


	@Id
	@Column(length = 11)
	public long getDrugId() {

		return drugId;
	}


	public void setDrugId(long drugId) {

		this.drugId = drugId;
	}


	@Column(length = 4)
	public int getDosageUnitCode() {

		return dosageUnitCode;
	}


	public void setDosageUnitCode(int dosageUnitCode) {

		this.dosageUnitCode = dosageUnitCode;
	}


	public void setValidFrom(Calendar validfrom) {

		this.validFrom = validfrom;
	}


	@Override
	public Calendar getValidFrom() {

		return validFrom;
	}
}
