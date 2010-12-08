package com.trifork.sdm.models.cpr;

import java.util.Calendar;

import com.trifork.sdm.models.AbstractEntity;
import com.trifork.sdm.util.DateUtils;


public abstract class CPREntity extends AbstractEntity {
	
	private String cpr;
	private Calendar validFrom;


	public String getCpr() {

		return cpr;
	}


	public void setCpr(String cpr) {

		this.cpr = cpr;
	}


	public void setValidFrom(Calendar validfrom) {

		this.validFrom = validfrom;
	}


	@Override
	public Calendar getValidTo() {

		return DateUtils.FUTURE;
	}


	@Override
	public Calendar getValidFrom() {

		return validFrom;
	}
}
