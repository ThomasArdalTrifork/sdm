package com.trifork.sdm.importer.persistence;

import java.util.Calendar;

import com.trifork.sdm.models.Record;


public interface TemporalStamdataEntityStorage<T extends Record>
{

	boolean fetchEntityVersions(Object entityId, Calendar validFrom, Calendar validTo);


	void insertRow(T entity, Calendar transactionTime);


	void insertAndUpdateRow(T entity, Calendar transactionTime);


	void updateRow(T sde, Calendar transactionTime, Calendar existingValidFrom,
			Calendar existingValidTo);


	Calendar getCurrentRowValidFrom();


	Calendar getCurrentRowValidTo();


	boolean dataInCurrentRowEquals(T entity);


	void copyCurrentRowButWithChangedValidFrom(Calendar validTo, Calendar now);


	void updateValidToOnCurrentRow(Calendar validFrom, Calendar now);


	void updateValidFromOnCurrentRow(Calendar validFrom, Calendar now);


	void deleteCurrentRow();


	boolean nextRow();


	int getUpdatedRecords();


	int getInsertedRows();


	int getDeletedRecords();


	void truncate();


	void drop();
}
