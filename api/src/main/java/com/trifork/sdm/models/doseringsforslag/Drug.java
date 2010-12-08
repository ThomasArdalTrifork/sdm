package com.trifork.sdm.models.doseringsforslag;

import java.util.Calendar;

import com.trifork.sdm.models.AbstractEntity;
import com.trifork.sdm.persistence.annotations.Id;
import com.trifork.sdm.persistence.annotations.Output;


@Output(documentation="Indeholder information om lægemidlers drug-id og doseringsenhed.")
public class Drug extends AbstractEntity {

	private Calendar validFrom;
	private int releaseNumber;
	private long drugId;
	private int dosageUnitCode;


	// Don't output this.
	// @Output(length=15)
	public int getReleaseNumber() {

		return releaseNumber;
	}


	public void setReleaseNumber(int releaseNumber) {

		this.releaseNumber = releaseNumber;
	}


	@Id
	@Output(length=11)
	public long getDrugId() {

		return drugId;
	}


	public void setDrugId(long drugId) {

		this.drugId = drugId;
	}


	@Output(length=4)
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
