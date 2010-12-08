package com.trifork.sdm.models.takst;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;


@Entity
public class Tidsenhed extends TakstEntity {

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
	public String getEntityId() {

		return enheder.getKode();
	}

}
