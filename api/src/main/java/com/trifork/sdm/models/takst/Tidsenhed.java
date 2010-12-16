package com.trifork.sdm.models.takst;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import com.trifork.sdm.models.AbstractRecord;
import com.trifork.sdm.models.takst.unused.DivEnheder;


@Entity
public class Tidsenhed extends AbstractRecord {
	
	private final DivEnheder enheder;


	public Tidsenhed(DivEnheder enheder) {

		this.enheder = enheder;
	}


	@Id
	@Column
	public String getTidsenhedKode() {

		return enheder.getKode();
	}


	@Column
	public String getTidsenhedTekst() {

		return enheder.getTekst();
	}


	@Override
	public String getKey() {

		return enheder.getKode();
	}
}
