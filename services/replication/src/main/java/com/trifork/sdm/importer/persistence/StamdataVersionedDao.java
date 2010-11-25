package com.trifork.sdm.importer.persistence;

import java.util.List;

import com.trifork.sdm.models.Entity;
import com.trifork.sdm.persistence.CompleteDataset;
import com.trifork.sdm.persistence.Dataset;


public interface StamdataVersionedDao
{

	/**
	 * This method is for persisting the complete dataset so it is represented
	 * in the Datasets validity interval (dataset.ValidTo-dataset.ValidFrom).
	 * Conflicts with existing data in this range are resolved by deleting or
	 * modifying the existing. In other words, the supplied dataset is treated
	 * as representing the whole truth within the datasets validity range.
	 * 
	 * If an existing entityVersion has validity in this datasets range, and is
	 * not part of this dataset, it will have this validity limited, by setting
	 * ValidTo to this datasets ValidFrom.
	 * 
	 * This dataset cannot overwrite existing records outside the datasets
	 * validity range. The Contained StamdateEntities are however persisted with
	 * a validTo exceeding the datasets, when no existing records exist in this
	 * range. For instance TakstEntities are valid for all eternity, or until
	 * another version exist.
	 * 
	 */
	public void persistCompleteDataset(CompleteDataset<? extends Entity> dataset)
			throws FilePersistException;


	/**
	 * Like persistCompleteDataset, but with multiple complete datasets.
	 */
	public void persistCompleteDatasets(List<CompleteDataset<? extends Entity>> dataset)
			throws FilePersistException;


	/**
	 * Persist the records in the dataset.
	 */
	public void persistDeltaDataset(Dataset<? extends Entity> dataset)
			throws FilePersistException;
}
