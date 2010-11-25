package com.trifork.sdm.persistence;

import java.util.Calendar;
import java.util.List;

import com.trifork.sdm.models.Entity;

/**
 * A Dataset that is the complete truth within the given validfrom-validto
 * interval. That is, no other records are allowed other than the ones in this
 * dataset.
 * 
 * @author rsl
 * 
 */
public class CompleteDataset<T extends Entity> extends Dataset<T>
{
	private final Calendar ValidFrom;
	private final Calendar ValidTo;


	protected CompleteDataset(Class<T> type, List<T> entities, Calendar validFrom, Calendar ValidTo)
	{
		super(entities, type);
		this.ValidFrom = validFrom;
		this.ValidTo = ValidTo;
	}


	public CompleteDataset(Class<? extends T> type, Calendar validFrom, Calendar ValidTo)
	{
		super(type);
		this.ValidFrom = validFrom;
		this.ValidTo = ValidTo;
	}


	public Calendar getValidFrom()
	{
		return ValidFrom;
	}


	public Calendar getValidTo()
	{
		return ValidTo;
	}

}
