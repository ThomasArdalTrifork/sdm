package com.trifork.sdm.models.takst.unused;

import com.trifork.sdm.models.AbstractRecord;

public class PakningskombinationerUdenPriser extends AbstractRecord {

	// Vnr. på pakningen anført på recepten.
	private Long varenummerOrdineret;

	// Vnr. på en pakning der evt. kan substitueres til.
	private Long varenummerSubstitueret;

	// Vnr. for en mindre, billigere pakning.
	private Long varenummerAlternativt;

	// Antal af den alternative pakning
	private Long antalPakninger;

	// Markering (stjerne *) for informationspligt.
	private String informationspligtMarkering;


	public Long getVarenummerOrdineret() {

		return this.varenummerOrdineret;
	}


	public void setVarenummerOrdineret(Long varenummerOrdineret) {

		this.varenummerOrdineret = varenummerOrdineret;
	}


	public Long getVarenummerSubstitueret() {

		return this.varenummerSubstitueret;
	}


	public void setVarenummerSubstitueret(Long varenummerSubstitueret) {

		this.varenummerSubstitueret = varenummerSubstitueret;
	}


	public Long getVarenummerAlternativt() {

		return this.varenummerAlternativt;
	}


	public void setVarenummerAlternativt(Long varenummerAlternativt) {

		this.varenummerAlternativt = varenummerAlternativt;
	}


	public Long getAntalPakninger() {

		return this.antalPakninger;
	}


	public void setAntalPakninger(Long antalPakninger) {

		this.antalPakninger = antalPakninger;
	}


	public String getInformationspligtMarkering() {

		return informationspligtMarkering;
	}


	public void setInformationspligtMarkering(String informationspligtMarkering) {

		this.informationspligtMarkering = informationspligtMarkering;
	}


	@Override
	public Object getKey() {

		return varenummerOrdineret;
	}
}