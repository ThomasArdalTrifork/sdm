package com.trifork.sdm.models;

import java.util.Calendar;


/**
 * Describes a contract that all models in SDM uphold.
 */
public interface Record {

	Object getRecordId();

	Calendar getValidFrom();

	Calendar getValidTo();
}
