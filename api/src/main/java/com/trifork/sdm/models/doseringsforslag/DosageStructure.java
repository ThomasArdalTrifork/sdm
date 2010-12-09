package com.trifork.sdm.models.doseringsforslag;

import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import com.trifork.sdm.Documented;
import com.trifork.sdm.models.AbstractRecord;


@Entity
@Documented("Indeholder doseringsstrukturer.")
public class DosageStructure extends AbstractRecord {

	// Reference til releaseNumber i Version. Obligatorisk. Heltal, 15 cifre.
	private long releaseNumber;

	// Unik kode for doseringstrukturen. Obligatorisk. Heltal, 11 cifre.
	private long code;

	// Typen af dosering, enten "M+M+A+N", "PN", "N daglig",
	// "Fritekst" eller "Kompleks". Obligatorisk. Streng, 100 tegn.
	private String type;

	// For simple typer (dvs. alt andet end "Kompleks")
	// indeholder feltet doseringen på simpel form. Optionelt. Streng, 100 tegn.
	private String simpleString;

	// For simple typer en eventuel supplerende tekst.
	// Optionelt. Streng, 200 tegn.
	private String supplementaryText;

	// FMKs strukturerede dosering i XML format. Bemærk at enkelte
	// værdier vil være escaped. Obligatorisk. Streng, 10000 tegn.
	private String xml;

	// Såfremt det er muligt at lave en kort
	// doseringstekst på baggrund af xml og lægemidlets doseringsenhed vil
	// dette felt indeholde denne. Optionelt. Streng, 70 tegn.
	private String shortTranslation;

	// En lang doseringstekst baggrund af xml og
	// lægemidlets doseringsenhed. Obligatorisk. Strengm 10000 tegn.
	private String longTranslation;

	private Calendar validFrom;


	public void setReleaseNumber(long releaseNumber) {

		this.releaseNumber = releaseNumber;
	}


	// Don't output this.
	// @Column(length=15)
	public long getReleaseNumber() {

		return releaseNumber;
	}


	public void setCode(long code) {

		this.code = code;
	}


	@Id
	@Column(length = 11)
	public long getCode() {

		return code;
	}


	public void setType(String type) {

		this.type = type;
	}


	@Column(length = 100)
	public String getType() {

		return type;
	}


	public void setSimpleString(String simpleString) {

		this.simpleString = simpleString;
	}


	@Column(length = 100)
	public String getSimpleString() {

		return simpleString;
	}


	public void setSupplementaryText(String supplementaryText) {

		this.supplementaryText = supplementaryText;
	}


	@Column(length = 200)
	public String getSupplementaryText() {

		return supplementaryText;
	}


	public void setXml(String xml) {

		this.xml = xml;
	}


	@Column(length = 10000)
	public String getXml() {

		return xml;
	}


	public void setShortTranslation(String shortTranslation) {

		this.shortTranslation = shortTranslation;
	}


	@Column(length = 70)
	public String getShortTranslation() {

		return shortTranslation;
	}


	public void setLongTranslation(String longTranslation) {

		this.longTranslation = longTranslation;
	}


	@Column(length = 10000)
	public String getLongTranslation() {

		return longTranslation;
	}


	@Override
	public Calendar getValidFrom() {

		return validFrom;
	}
}
