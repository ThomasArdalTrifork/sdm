package com.trifork.sdm.importer.importers.autorisationsregisteret;

import java.util.Date;

import com.trifork.sdm.models.autorisationsregisteret.Autorisation;
import com.trifork.sdm.persistence.CompleteDataset;
import com.trifork.sdm.util.DateUtils;


public class Autorisationsregisterudtraek extends CompleteDataset<Autorisation> {
	
	public Autorisationsregisterudtraek(Date validFrom) {

		super(Autorisation.class, validFrom, DateUtils.FUTURE);
	}


	@Override
	public void addRecord(Autorisation aut) {

		aut.setValidFrom(getValidFrom());
		super.addRecord(aut);
	}
}
