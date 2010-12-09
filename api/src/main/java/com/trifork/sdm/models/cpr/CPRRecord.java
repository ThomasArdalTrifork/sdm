package com.trifork.sdm.models.cpr;

import com.trifork.sdm.models.AbstractRecord;


public abstract class CPRRecord extends AbstractRecord {

	private String cpr;

	public String getCpr() {
		
		return cpr;
	}


	public void setCpr(String cpr) {

		this.cpr = cpr;
	}
}
