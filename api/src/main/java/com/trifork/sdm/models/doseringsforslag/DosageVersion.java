package com.trifork.sdm.models.doseringsforslag;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import com.trifork.sdm.models.AbstractRecord;
import com.trifork.sdm.models.Documented;


@Entity
@Documented("Indeholder versioneringsinformation.")
public class DosageVersion extends AbstractRecord {

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

	private Date validFrom;


	@Column
	public Date getDaDate() {

		return daDate;
	}


	@Column
	public Date getLmsDate() {

		return lmsDate;
	}


	@Id
	@Column
	public Date getReleaseDate() {

		return releaseDate;
	}


	// Don't output this.
	// @Column(length=15)
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


	@Override
	public void setValidFrom(Date validfrom) {

		this.validFrom = validfrom;
	}


	@Override
	public Date getValidFrom() {

		return validFrom;
	}
}
