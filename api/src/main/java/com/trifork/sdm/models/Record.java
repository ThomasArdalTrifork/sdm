package com.trifork.sdm.models;

import java.util.Date;


public abstract class Record {

	public abstract long getPID();
	public abstract Object getKey();
	public abstract Date getValidFrom();
	public abstract Date getValidTo();
}
