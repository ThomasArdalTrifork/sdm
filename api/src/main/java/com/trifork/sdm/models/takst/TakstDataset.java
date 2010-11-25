package com.trifork.sdm.models.takst;

import java.util.Calendar;
import java.util.List;

import org.apache.log4j.Logger;

import com.trifork.sdm.persistence.CompleteDataset;

public class TakstDataset<T extends TakstEntity> extends CompleteDataset<T>
{
	private Takst takst;
	Logger logger = Logger.getLogger(getClass());


	public TakstDataset(Takst takst, List<T> entities, Class<T> type)
	{
		super(type, entities, takst.getValidFrom(), takst.getValidTo());
		for (TakstEntity entity : entities)
		{
			entity.takst = takst;
		}
		this.takst = takst;
	}


	@Override
	public Calendar getValidFrom()
	{
		return takst.getValidFrom();
	}


	@Override
	public Calendar getValidTo()
	{
		return takst.getValidTo();
	}


	@Override
	public void addEntity(T entity)
	{
		super.addEntity(entity);
		entity.takst = takst;

	}

}
