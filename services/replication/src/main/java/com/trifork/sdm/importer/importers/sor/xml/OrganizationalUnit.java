package com.trifork.sdm.importer.importers.sor.xml;

import java.util.Calendar;


public class OrganizationalUnit extends AddressInformation
{
	private Long sorIdentifier;
	private String entityName;
	private Long unitType;
	private String pharmacyIdentifier;
	private String shakIdentifier;
	private Long specialityCode;
	private String providerIdentifier;
	private Calendar fromDate;
	private Calendar toDate;


	public OrganizationalUnit()
	{
		super();
	}


	public Long getSorIdentifier()
	{
		return sorIdentifier;
	}


	public void setSorIdentifier(Long sorIdentifier)
	{
		this.sorIdentifier = sorIdentifier;
	}


	public String getEntityName()
	{
		return entityName;
	}


	public void setEntityName(String entityName)
	{
		this.entityName = entityName;
	}


	public Long getUnitType()
	{
		return unitType;
	}


	public void setUnitType(Long unitType)
	{
		this.unitType = unitType;
	}


	public String getPharmacyIdentifier()
	{
		return pharmacyIdentifier;
	}


	public void setPharmacyIdentifier(String pharmacyIdentifier)
	{
		this.pharmacyIdentifier = pharmacyIdentifier;
	}


	public String getShakIdentifier()
	{
		return shakIdentifier;
	}


	public void setShakIdentifier(String shakIdentifier)
	{
		this.shakIdentifier = shakIdentifier;
	}


	public Long getSpecialityCode()
	{
		return specialityCode;
	}


	public void setSpecialityCode(Long specialityCode)
	{
		this.specialityCode = specialityCode;
	}


	public String getProviderIdentifier()
	{
		return providerIdentifier;
	}


	public void setProviderIdentifier(String providerIdentifier)
	{
		this.providerIdentifier = providerIdentifier;
	}


	public Calendar getFromDate()
	{
		return fromDate;
	}


	public void setFromDate(Calendar validFrom)
	{
		this.fromDate = validFrom;
	}


	public Calendar getToDate()
	{
		return toDate;
	}


	public void setToDate(Calendar toDate)
	{
		this.toDate = toDate;
	}

}
