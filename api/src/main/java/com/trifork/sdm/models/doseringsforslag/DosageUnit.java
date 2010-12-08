package com.trifork.sdm.models.doseringsforslag;

import java.util.Calendar;

import com.trifork.sdm.models.AbstractEntity;
import com.trifork.sdm.persistence.annotations.Id;
import com.trifork.sdm.persistence.annotations.Output;


@Output(documentation = "Indeholder anvendte doseringsenheder.\n"
		+ "Doseringsenhederne stammer dels fra Lægemiddelstyrelsens takst (her er code <= 1000),\n"
		+ "dels er der tale om nye data (code > 1000).")
public class DosageUnit extends AbstractEntity {

	// Reference til releaseNumber i Version. Obligatorisk. Heltal, 15 cifre.
	private long releaseNumber;

	// Unik kode for doseringsenheden. Obligatorisk. Heltal, 4 cifre.
	private int code;

	// Doseringenhedens tekst i ental. Obligatorisk. Streng, 100 tegn.
	private String textSingular;

	// Doseringsenhedens tekst i flertal. Obligatorisk. Streng, 100 tegn.
	private String textPlural;

	private Calendar validFrom;


	public void setReleaseNumber(long releaseNumber) {

		this.releaseNumber = releaseNumber;
	}


	// Don't output this.
	// @Output(length = 15)
	public long getReleaseNumber() {

		return releaseNumber;
	}


	public void setCode(int code) {

		this.code = code;
	}


	@Id
	@Output(length = 4)
	public int getCode() {

		return code;
	}


	public void setTextSingular(String textSingular) {

		this.textSingular = textSingular;
	}


	@Output(length = 100)
	public String getTextSingular() {

		return textSingular;
	}


	public void setTextPlural(String textPlural) {

		this.textPlural = textPlural;
	}


	@Output(length = 100)
	public String getTextPlural() {

		return textPlural;
	}


	@Override
	public Calendar getValidFrom() {

		return validFrom;
	}
}
