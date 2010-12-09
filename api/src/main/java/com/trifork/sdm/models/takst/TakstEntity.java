package com.trifork.sdm.models.takst;

import java.util.Calendar;

import com.trifork.sdm.models.AbstractRecord;


public abstract class TakstEntity extends AbstractRecord {
	protected Takst takst;


	@Override
	public Calendar getValidFrom() {

		return takst.getValidFrom();
	}
}
