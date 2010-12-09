package com.trifork.sdm.importer.importers.autorisationsregisteret;

import java.util.Calendar;

import com.trifork.sdm.models.AbstractRecord;
import com.trifork.sdm.models.autorisationsregisteret.Autorisation;
import com.trifork.sdm.persistence.CompleteDataset;


public class Autorisationsregisterudtraek extends CompleteDataset<Autorisation>
{
	public Autorisationsregisterudtraek(Calendar validFrom)
	{
		super(Autorisation.class, validFrom, AbstractRecord.FUTURE);
	}


	@Override
	public void addEntity(Autorisation aut)
	{
		aut.setValidFrom(getValidFrom());
		super.addEntity(aut);
	}
}
