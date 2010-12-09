package com.trifork.sdm.models;

import java.util.Date;


/**
 * Describes a contract that all models in SDM uphold.
 */
public interface Record {

	Object getRecordId();

	Date getValidFrom();

	Date getValidTo();
}
