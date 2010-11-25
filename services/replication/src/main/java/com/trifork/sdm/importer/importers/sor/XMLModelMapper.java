package com.trifork.sdm.importer.importers.sor;

import com.trifork.sdm.importer.importers.sor.xml.HealthInstitutionEntity;
import com.trifork.sdm.importer.importers.sor.xml.OrganizationalUnitEntity;
import com.trifork.sdm.importer.importers.sor.xml.SpecialityMapper;
import com.trifork.sdm.importer.importers.sor.xml.UnitTypeMapper;
import com.trifork.sdm.models.sor.Apotek;
import com.trifork.sdm.models.sor.Praksis;
import com.trifork.sdm.models.sor.Sygehus;
import com.trifork.sdm.models.sor.SygehusAfdeling;
import com.trifork.sdm.models.sor.Yder;


public class XMLModelMapper
{

	public static Praksis toPraksis(HealthInstitutionEntity hie)
	{
		Praksis p = new Praksis();
		
		p.setNavn(hie.getEntityName());
		p.setSorNummer(hie.getSorIdentifier());
		p.setEanLokationsnummer(hie.getEanLocationCode());
		p.setValidFrom(hie.getFromDate());
		p.setValidTo(hie.getToDate());
		p.setRegionCode(hie.getInstitutionOwnerEntity().getRegionCode());

		return p;
	}


	public static Yder toYder(OrganizationalUnitEntity oue)
	{
		Yder y = new Yder();
		y.setSorNummer(oue.getSorIdentifier());
		y.setPraksisSorNummer(oue.getHealthInstitutionEntity().getSorIdentifier());
		y.setEanLokationsnummer(oue.getEanLocationCode());
		y.setNavn(oue.getEntityName());
		if (oue.getProviderIdentifier() != null)
		{
			y.setNummer(oue.getProviderIdentifier().replaceAll("^0+(?!$)", ""));
		}
		y.setVejnavn(oue.getStreetName() + " " + oue.getStreetBuildingIdentifier());
		y.setBynavn(oue.getDistrictName());
		y.setPostnummer(oue.getPostCodeIdentifier());
		y.setEmail(oue.getEmailAddressIdentifier());
		y.setWww(oue.getWebsite());
		y.setTelefon(oue.getTelephoneNumberIdentifier());
		y.setHovedSpecialeKode(oue.getSpecialityCode());
		y.setHovedSpecialeTekst(SpecialityMapper.kodeToString(oue.getSpecialityCode()));
		y.setValidFrom(oue.getFromDate());
		y.setValidTo(oue.getToDate());
		return y;
	}


	public static Sygehus toSygehus(HealthInstitutionEntity hie)
	{
		Sygehus s = new Sygehus();
		s.setSorNummer(hie.getSorIdentifier());
		s.setNavn(hie.getEntityName());
		s.setEanLokationsnummer(hie.getEanLocationCode());
		s.setNummer(hie.getShakIdentifier());
		s.setVejnavn(hie.getStreetName() + " " + hie.getStreetBuildingIdentifier());
		s.setBynavn(hie.getDistrictName());
		s.setPostnummer(hie.getPostCodeIdentifier());
		s.setEmail(hie.getEmailAddressIdentifier());
		s.setWww(hie.getWebsite());
		s.setTelefon(hie.getTelephoneNumberIdentifier());
		s.setValidFrom(hie.getFromDate());
		s.setValidTo(hie.getToDate());

		return s;
	}


	public static SygehusAfdeling toSygehusAfdeling(OrganizationalUnitEntity oue)
	{
		SygehusAfdeling sa = new SygehusAfdeling();
		sa.setEanLokationsnummer(oue.getEanLocationCode());
		sa.setSorNummer(oue.getSorIdentifier());
		sa.setNavn(oue.getEntityName());
		sa.setNummer(oue.getShakIdentifier());
		sa.setVejnavn(oue.getStreetName() + " " + oue.getStreetBuildingIdentifier());
		sa.setBynavn(oue.getDistrictName());
		sa.setPostnummer(oue.getPostCodeIdentifier());
		sa.setEmail(oue.getEmailAddressIdentifier());
		sa.setWww(oue.getWebsite());
		sa.setTelefon(oue.getTelephoneNumberIdentifier());
		sa.setAfdelingTypeKode(oue.getUnitType());
		sa.setAfdelingTypeTekst(UnitTypeMapper.kodeToString(oue.getUnitType()));
		sa.setHovedSpecialeKode(oue.getSpecialityCode());
		sa.setHovedSpecialeTekst(SpecialityMapper.kodeToString(oue.getSpecialityCode()));
		if (oue.getParrent() != null)
		{
			// Subdivision of an other 'afdeling'
			sa.setOverAfdelingSorNummer(oue.getParrent().getSorIdentifier());
		}
		else
		{
			// Directly under a 'Sygehus'
			sa.setSygehusSorNummer(oue.getHealthInstitutionEntity().getSorIdentifier());
		}
		sa.setUnderlagtSygehusSorNummer(oue.getBelongsTo().getSorIdentifier());
		sa.setValidFrom(oue.getFromDate());
		sa.setValidTo(oue.getToDate());
		return sa;
	}


	public static Apotek toApotek(OrganizationalUnitEntity oue)
	{
		Apotek a = new Apotek();
		
		a.setSorNummer(oue.getSorIdentifier());
		
		if (oue.getPharmacyIdentifier() != null)
		{
			String[] pi = oue.getPharmacyIdentifier().split(",");
			a.setApotekNummer(Long.parseLong(pi[0]));
			if (pi.length > 1)
			{
				a.setFilialNummer(Long.parseLong(pi[1]));
			}

		}
		
		a.setEanLokationsnummer(oue.getEanLocationCode());
		a.setNavn(oue.getEntityName());
		a.setVejnavn(oue.getStreetName() + " " + oue.getStreetBuildingIdentifier());
		a.setBynavn(oue.getDistrictName());
		a.setPostnummer(oue.getPostCodeIdentifier());
		a.setEmail(oue.getEmailAddressIdentifier());
		a.setWww(oue.getWebsite());
		a.setTelefon(oue.getTelephoneNumberIdentifier());
		a.setValidFrom(oue.getFromDate());
		a.setValidTo(oue.getToDate());
		
		return a;
	}
}
