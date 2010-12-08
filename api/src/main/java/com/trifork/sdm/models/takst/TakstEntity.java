package com.trifork.sdm.models.takst;

import java.util.Calendar;

import com.trifork.sdm.models.AbstractEntity;


public abstract class TakstEntity extends AbstractEntity {
	protected Takst takst;


	@Override
	public Calendar getValidFrom() {

		return takst.getValidFrom();
	}
}
