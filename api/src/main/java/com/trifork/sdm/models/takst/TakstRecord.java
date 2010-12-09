package com.trifork.sdm.models.takst;

import java.util.Date;

import com.trifork.sdm.models.AbstractRecord;


public abstract class TakstRecord extends AbstractRecord {

	protected Takst takst;


	@Override
	public Date getValidFrom() {

		return takst.getValidFrom();
	}
}
