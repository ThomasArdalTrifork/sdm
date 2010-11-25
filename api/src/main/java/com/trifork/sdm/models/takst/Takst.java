package com.trifork.sdm.models.takst;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.trifork.sdm.models.Entity;
import com.trifork.sdm.persistence.CompleteDataset;
import com.trifork.sdm.persistence.Dataset;
import com.trifork.sdm.persistence.annotations.Id;
import com.trifork.sdm.persistence.annotations.Output;


/**
 * A version of the Takst.
 * TODO: More descriptive please.
 */
@Output(name = "TakstVersion")
public class Takst extends TakstEntity
{
	private final List<CompleteDataset<? extends Entity>> datasets = new ArrayList<CompleteDataset<? extends Entity>>();

	// The week-number for which LMS guarantees some sort of stability/validity
	// for a subset of this rate. (The stable subset excludes pricing and
	// substitutions and possibly more)
	private int validityYear, validityWeekNumber;
	
	private Calendar validFrom;
	private Calendar validTo;


	public Takst(Calendar validFrom, Calendar validTo)
	{
		this.validFrom = validFrom;
		this.validTo = validTo;
	}


	/**
	 * @param type
	 *            the Type that the returned entities should have.
	 * 
	 * @return All entities of the given type in this takst.
	 */
	@SuppressWarnings("unchecked")
	public <T extends TakstEntity> TakstDataset<T> getDatasetOfType(Class<T> type)
	{
		for (CompleteDataset<? extends Entity> dataset : datasets)
		{
			if (type.equals(dataset.getType()))
			{
				return (TakstDataset<T>) dataset;
			}
		}

		return null;
	}


	public List<CompleteDataset<? extends Entity>> getDatasets()
	{
		return datasets;
	}


	public List<Entity> getEntities()
	{
		List<Entity> result = new ArrayList<Entity>();

		for (CompleteDataset<? extends Entity> dataset : datasets)
		{
			result.addAll(dataset.getEntities());
		}

		return result;
	}


	// TODO: What should the type argument be here?
	public void addDataset(TakstDataset<?> dataset)
	{
		datasets.add(dataset);
	}


	/**
	 * @param type
	 *            the type of the requested entity
	 * @param entityId
	 *            the id of the requested entity
	 * @return the requested entity
	 */
	public <T extends TakstEntity> T getEntity(Class<T> type, Object entityId)
	{
		if (entityId == null) return null;

		Dataset<T> avds = getDatasetOfType(type);

		if (avds == null) return null;

		return avds.getEntityById(entityId);
	}


	@Output(name = "TakstUge")
	public String getStableWeek()
	{
		return "" + validityYear + validityWeekNumber;
	}


	public void setValidityYear(int validityYear)
	{
		this.validityYear = validityYear;
	}


	public void setValidityWeekNumber(int validityWeekNumber)
	{
		this.validityWeekNumber = validityWeekNumber;
	}

	
	@Id
	public Calendar getValidFrom()
	{
		return validFrom;
	}


	public Calendar getValidTo()
	{
		return validTo;
	}
}
