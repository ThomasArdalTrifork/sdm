package com.trifork.sdm.models.doseringsforslag;

import java.util.Calendar;

import com.trifork.sdm.models.AbstractEntity;
import com.trifork.sdm.persistence.annotations.Id;
import com.trifork.sdm.persistence.annotations.Output;

@Output(documentation="Indeholder doseringsstrukturer.")
public class DosageStructure extends AbstractEntity {
	
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
	// @Output(length=15)
	public long getReleaseNumber() {

		return releaseNumber;
	}

	public void setCode(long code) {

		this.code = code;
	}

	@Id
	@Output(length=11)
	public long getCode() {

		return code;
	}

	public void setType(String type) {

		this.type = type;
	}

	@Output(length=100)
	public String getType() {

		return type;
	}

	public void setSimpleString(String simpleString) {

		this.simpleString = simpleString;
	}

	@Output(length=100)
	public String getSimpleString() {

		return simpleString;
	}

	public void setSupplementaryText(String supplementaryText) {

		this.supplementaryText = supplementaryText;
	}

	@Output(length=200)
	public String getSupplementaryText() {

		return supplementaryText;
	}

	public void setXml(String xml) {

		this.xml = xml;
	}

	@Output(length=10000)
	public String getXml() {

		return xml;
	}

	public void setShortTranslation(String shortTranslation) {

		this.shortTranslation = shortTranslation;
	}

	@Output(length=70)
	public String getShortTranslation() {

		return shortTranslation;
	}

	public void setLongTranslation(String longTranslation) {

		this.longTranslation = longTranslation;
	}

	@Output(length=10000)
	public String getLongTranslation() {

		return longTranslation;
	}

	@Override
	public Calendar getValidFrom() {

		return validFrom;
	}
}
