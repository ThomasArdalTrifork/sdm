package com.trifork.sdm.models.takst.unused;

import javax.persistence.Column;

import com.trifork.sdm.models.AbstractRecord;


public class NumeriskMedEnhed extends AbstractRecord {
	
	private String klartekst;
	private double numerisk;
	private Object enhed;


	public NumeriskMedEnhed(String klartekst, double numerisk, Object enhed) {

		this.klartekst = klartekst;
		this.numerisk = numerisk;
		this.enhed = enhed;
	}


	@Override
	public String getKey() {

		return null;
	}


	@Column(name = "StyrkeTekst")
	public String getKlartekst() {

		return klartekst;
	}


	@Column(name = "StyrkeNumerisk")
	public double getNumerisk() {

		return numerisk;
	}


	@Column(name = "StyrkeEnhed")
	public String getEnhed() {

		if (enhed instanceof DivEnheder) return ((DivEnheder) enhed).getTekst();
		return null;
	}


	/**
	 * Only used when enhed is a String
	 */
	@Column(name = "StyrkeEnhed")
	public String getEnhedString() {

		if (enhed instanceof String) return (String) enhed;
		return null;
	}


	public String getEntityTypeDisplayName() {

		// Should probably never be used as objects of this class are always
		// nested
		return getClass().getSimpleName();
	}

}
