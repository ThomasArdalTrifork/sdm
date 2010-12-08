package com.trifork.sdm.models.doseringsforslag;

import java.util.Calendar;
import java.util.Date;

import com.trifork.sdm.models.AbstractEntity;
import com.trifork.sdm.persistence.annotations.Id;
import com.trifork.sdm.persistence.annotations.Output;


@Output(documentation = "Indeholder versioneringsinformation.")
public class DosageVersion extends AbstractEntity {

	// daDate: Dato for Apotekerforeningens mærkevaretakst, som datasættet er
	// udarbejdet på baggrund af. Obligatorisk. Dato, yyyy-MM-dd.
	private Date daDate;

	// lmsDate: Dato for Lægemiddelstyrelsens takst, som datasættet er
	// udarbejdet på baggrund af. Obligatorisk. Dato, yyyy-MM-dd.
	private Date lmsDate;

	// Dato filen er released. Obligatorisk. Dato, yyyy-MM-dd.
	private Date releaseDate;

	// Unikt release nummer. Obligatorisk. Heltal, 15 cifre.
	private long releaseNumber;

	private Calendar validFrom;

	@Output
	public Date getDaDate() {
	
		return daDate;
	}


	@Output
	public Date getLmsDate() {
	
		return lmsDate;
	}

	@Id
	@Output
	public Date getReleaseDate() {
	
		return releaseDate;
	}

	// Don't output this.
	// @Output(length=15)
	public long getReleaseNumber() {

		return releaseNumber;
	}


	public void setDaDate(Date daDate) {
	
		this.daDate = daDate;
	}


	public void setLmsDate(Date lmsDate) {
	
		this.lmsDate = lmsDate;
	}

	public void setReleaseDate(Date releaseDate) {
	
		this.releaseDate = releaseDate;
	}


	public void setReleaseNumber(long releaseNumber) {

		this.releaseNumber = releaseNumber;
	}
	


	public void setValidFrom(Calendar validfrom) {

		this.validFrom = validfrom;
	}


	@Override
	public Calendar getValidFrom() {

		return validFrom;
	}
}
