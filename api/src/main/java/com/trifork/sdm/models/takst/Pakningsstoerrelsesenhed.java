package com.trifork.sdm.models.takst;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import com.trifork.sdm.models.AbstractRecord;
import com.trifork.sdm.models.takst.unused.DivEnheder;


@Entity
public class Pakningsstoerrelsesenhed extends AbstractRecord {

	private final DivEnheder enheder;


	public Pakningsstoerrelsesenhed(DivEnheder enheder) {

		this.enheder = enheder;
	}


	@Id
	@Column
	public String getPakningsstoerrelsesenhedKode() {

		return enheder.getKode();
	}


	@Column
	public String getPakningsstoerrelsesenhedTekst() {

		return enheder.getTekst();
	}


	@Override
	public String getKey() {

		return enheder.getKode();
	}
}
