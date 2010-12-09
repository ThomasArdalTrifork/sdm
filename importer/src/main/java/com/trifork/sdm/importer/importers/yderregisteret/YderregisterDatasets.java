package com.trifork.sdm.importer.importers.yderregisteret;

import java.util.Calendar;

import com.trifork.sdm.models.AbstractRecord;
import com.trifork.sdm.models.yderregisteret.Yderregister;
import com.trifork.sdm.models.yderregisteret.YderregisterPerson;
import com.trifork.sdm.persistence.CompleteDataset;

public class YderregisterDatasets
{
	private final CompleteDataset<Yderregister> yderregisterDS;
	private final CompleteDataset<YderregisterPerson> yderregisterPersonDS;


	public YderregisterDatasets(Calendar validFrom)
	{
		yderregisterDS = new CompleteDataset<Yderregister>(Yderregister.class, validFrom,
				AbstractRecord.FUTURE);
		yderregisterPersonDS = new CompleteDataset<YderregisterPerson>(YderregisterPerson.class,
				validFrom, AbstractRecord.FUTURE);
	}


	public CompleteDataset<Yderregister> getYderregisterDS()
	{
		return yderregisterDS;
	}


	public CompleteDataset<YderregisterPerson> getYderregisterPersonDS()
	{
		return yderregisterPersonDS;
	}


	public void addYderregister(Yderregister entity)
	{
		yderregisterDS.addEntity(entity);
	}


	public void addYderregisterPerson(YderregisterPerson entity)
	{
		yderregisterPersonDS.addEntity(entity);
	}
}
