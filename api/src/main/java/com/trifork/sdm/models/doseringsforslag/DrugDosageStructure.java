package com.trifork.sdm.models.doseringsforslag;

import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import com.trifork.sdm.Documented;
import com.trifork.sdm.models.AbstractRecord;


@Entity
@Documented("Referencetabel der knytter doseringsstrukturer i dosageStructures til lægemidler.")
public class DrugDosageStructure extends AbstractRecord {

	// Reference til releaseNumber i Version. Obligatorisk. Heltal, 15 cifre.
	private long releaseNumber;

	// Lægemidlets drug id. Reference til drugId i drugs. Obligatorisk. Heltal,
	// 11 cifre.
	private long drugId;

	// Reference til code i dosageStructure. Obligatorisk. Heltal, 11 cifre.
	private long dosageStructureCode;

	private Calendar validFrom;


	public void setReleaseNumber(long releaseNumber) {

		this.releaseNumber = releaseNumber;
	}


	@Id
	@Column(length = 22)
	public String getId() {

		return Long.toString(drugId) + Long.toString(dosageStructureCode);
	}


	// Don't output this.
	// @Column(length=15)
	public long getReleaseNumber() {

		return releaseNumber;
	}


	public void setDrugId(long drugId) {

		this.drugId = drugId;
	}


	@Column(length = 11)
	public long getDrugId() {

		return drugId;
	}


	public void setDosageStructureCode(long dosageStructureCode) {

		this.dosageStructureCode = dosageStructureCode;
	}


	@Column(length = 11)
	public long getDosageStructureCode() {

		return dosageStructureCode;
	}


	@Override
	public Calendar getValidFrom() {

		return validFrom;
	}
}
