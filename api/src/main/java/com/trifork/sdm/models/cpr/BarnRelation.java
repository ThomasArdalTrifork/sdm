package com.trifork.sdm.models.cpr;

import com.trifork.sdm.persistence.annotations.Id;
import com.trifork.sdm.persistence.annotations.Output;


@Output
public class BarnRelation extends CPREntity {
	String barnCpr;


	@Id
	@Output
	public String getId() {

		return getCpr() + "-" + barnCpr;
	}


	@Output
	@Override
	public String getCpr() {

		return super.getCpr();
	}


	@Output
	public String getBarnCpr() {

		return barnCpr;
	}


	public void setBarnCpr(String barnCpr) {

		this.barnCpr = barnCpr;
	}
}
