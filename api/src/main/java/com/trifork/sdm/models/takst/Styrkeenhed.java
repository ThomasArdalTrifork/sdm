package com.trifork.sdm.models.takst;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import com.trifork.sdm.models.AbstractRecord;
import com.trifork.sdm.models.takst.unused.DivEnheder;


@Entity
public class Styrkeenhed extends AbstractRecord {
	
	private final DivEnheder enheder;


	public Styrkeenhed(DivEnheder enheder) {

		this.enheder = enheder;
	}


	@Id
	@Column
	public String getStyrkeenhedKode() {

		return enheder.getKode();
	}


	@Column
	public String getStyrkeenhedTekst() {

		return enheder.getTekst();
	}


	@Override
	public String getKey() {

		return enheder.getKode();
	}

}