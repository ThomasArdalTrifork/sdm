package com.trifork.sdm.importer.importers.yderregisteret;

import java.util.Date;

import com.trifork.sdm.models.yderregisteret.Yderregister;
import com.trifork.sdm.models.yderregisteret.YderregisterPerson;
import com.trifork.sdm.persistence.CompleteDataset;
import com.trifork.sdm.util.DateUtils;


public class YderregisterDatasets {
	private final CompleteDataset<Yderregister> yderregisterDS;
	private final CompleteDataset<YderregisterPerson> yderregisterPersonDS;


	public YderregisterDatasets(Date validFrom) {

		yderregisterDS = new CompleteDataset<Yderregister>(Yderregister.class, validFrom, DateUtils.FUTURE);
		yderregisterPersonDS = new CompleteDataset<YderregisterPerson>(YderregisterPerson.class, validFrom,
				DateUtils.FUTURE);
	}


	public CompleteDataset<Yderregister> getYderregisterDS() {

		return yderregisterDS;
	}


	public CompleteDataset<YderregisterPerson> getYderregisterPersonDS() {

		return yderregisterPersonDS;
	}


	public void addYderregister(Yderregister entity) {

		yderregisterDS.addRecord(entity);
	}


	public void addYderregisterPerson(YderregisterPerson entity) {

		yderregisterPersonDS.addRecord(entity);
	}
}
