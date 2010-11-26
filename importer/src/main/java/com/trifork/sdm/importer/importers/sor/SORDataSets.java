package com.trifork.sdm.importer.importers.sor;

import com.trifork.sdm.models.sor.Apotek;
import com.trifork.sdm.models.sor.Praksis;
import com.trifork.sdm.models.sor.Sygehus;
import com.trifork.sdm.models.sor.SygehusAfdeling;
import com.trifork.sdm.models.sor.Yder;
import com.trifork.sdm.persistence.CompleteDataset;


public class SORDataSets
{
	private CompleteDataset<Apotek> apotekDS;
	private CompleteDataset<Yder> yderDS;
	private CompleteDataset<Praksis> praksisDS;
	private CompleteDataset<Sygehus> sygehusDS;
	private CompleteDataset<SygehusAfdeling> sygehusAfdelingDS;


	public CompleteDataset<Apotek> getApotekDS()
	{
		return apotekDS;
	}


	public void setApotekDS(CompleteDataset<Apotek> apotekDS)
	{
		this.apotekDS = apotekDS;
	}


	public CompleteDataset<Yder> getYderDS()
	{
		return yderDS;
	}


	public void setYderDS(CompleteDataset<Yder> yderDS)
	{
		this.yderDS = yderDS;
	}


	public CompleteDataset<Praksis> getPraksisDS()
	{
		return praksisDS;
	}


	public void setPraksisDS(CompleteDataset<Praksis> praksisDS)
	{
		this.praksisDS = praksisDS;
	}


	public CompleteDataset<Sygehus> getSygehusDS()
	{
		return sygehusDS;
	}


	public void setSygehusDS(CompleteDataset<Sygehus> sygehusDS)
	{
		this.sygehusDS = sygehusDS;
	}


	public CompleteDataset<SygehusAfdeling> getSygehusAfdelingDS()
	{
		return sygehusAfdelingDS;
	}


	public void setSygehusAfdelingDS(CompleteDataset<SygehusAfdeling> sygehusAfdelingDS)
	{
		this.sygehusAfdelingDS = sygehusAfdelingDS;
	}

}
