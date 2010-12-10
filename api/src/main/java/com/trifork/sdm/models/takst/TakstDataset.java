package com.trifork.sdm.models.takst;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.trifork.sdm.persistence.CompleteDataset;


public class TakstDataset<T extends TakstRecord> extends CompleteDataset<T> {
	private Takst takst;
	Logger logger = Logger.getLogger(getClass());


	public TakstDataset(Takst takst, List<T> entities, Class<T> type) {

		super(type, entities, takst.getValidFrom(), takst.getValidTo());
		
		for (TakstRecord entity : entities) {
			entity.takst = takst;
		}
		
		this.takst = takst;
	}


	@Override
	public Date getValidFrom() {

		return takst.getValidFrom();
	}


	@Override
	public Date getValidTo() {

		return takst.getValidTo();
	}


	@Override
	public void addRecord(T entity) {

		super.addRecord(entity);
		entity.takst = takst;

	}
}
