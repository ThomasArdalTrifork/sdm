package com.trifork.sdm.models.sks;

import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import com.trifork.sdm.models.AbstractEntity;


@Entity
public class Organisation extends AbstractEntity {
	private String navn;
	private String nummer;

	private Calendar validFrom;
	private Calendar validTo;

	private final OrgatizationType orgatizationType;


	public enum OrgatizationType {
		Department("Afdeling"), Hospital("Sygehus");

		private String text;


		private OrgatizationType(String text) {

			this.text = text;
		}


		@Override
		public String toString() {

			return text;
		}
	}


	public Organisation(OrgatizationType orgatizationType) {

		this.orgatizationType = orgatizationType;
	}


	@Id
	@Column
	public String getNummer() {

		return nummer;
	}


	public void setNummer(String nummer) {

		this.nummer = nummer;
	}


	@Column
	public String getNavn() {

		return navn;
	}


	public void setNavn(String navn) {

		this.navn = navn;
	}


	public Calendar getValidTo() {

		return validTo;
	}


	public void setValidTo(Calendar validTo) {

		this.validTo = validTo;
	}


	@Override
	public Calendar getValidFrom() {

		return validFrom;
	}


	@Column
	public String getOrganisationstype() {

		return orgatizationType.toString();
	}


	public void setValidFrom(Calendar validFrom) {

		this.validFrom = validFrom;
	}
}
