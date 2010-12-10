package com.trifork.sdm.importer.importers.cpr;

import java.util.Date;

import com.trifork.sdm.models.cpr.BarnRelation;
import com.trifork.sdm.models.cpr.CPRRecord;
import com.trifork.sdm.models.cpr.ForaeldreMyndighedRelation;
import com.trifork.sdm.models.cpr.Klarskriftadresse;
import com.trifork.sdm.models.cpr.NavneBeskyttelse;
import com.trifork.sdm.models.cpr.Navneoplysninger;
import com.trifork.sdm.models.cpr.Personoplysninger;
import com.trifork.sdm.models.cpr.UmyndiggoerelseVaergeRelation;
import com.trifork.sdm.persistence.Dataset;


public class CPRDataset {

	private Dataset<Personoplysninger> personoplysninger = new Dataset<Personoplysninger>(
			Personoplysninger.class);
	private Dataset<Klarskriftadresse> klarskriftadresse = new Dataset<Klarskriftadresse>(
			Klarskriftadresse.class);
	private Dataset<NavneBeskyttelse> navneBeskyttelse = new Dataset<NavneBeskyttelse>(NavneBeskyttelse.class);
	private Dataset<Navneoplysninger> navneoplysninger = new Dataset<Navneoplysninger>(Navneoplysninger.class);
	private Dataset<UmyndiggoerelseVaergeRelation> umyndiggoerelseVaergeRelation = new Dataset<UmyndiggoerelseVaergeRelation>(
			UmyndiggoerelseVaergeRelation.class);
	private Dataset<ForaeldreMyndighedRelation> foraeldreMyndighedRelation = new Dataset<ForaeldreMyndighedRelation>(
			ForaeldreMyndighedRelation.class);
	private Dataset<BarnRelation> barnRelation = new Dataset<BarnRelation>(BarnRelation.class);

	private Date validFrom, previousFileValidFrom;


	public Date getValidFrom() {

		return validFrom;
	}


	public void setValidFrom(Date validFrom) {

		this.validFrom = validFrom;
	}


	public Date getPreviousFileValidFrom() {

		return previousFileValidFrom;
	}


	public void setPreviousFileValidFrom(Date previousFileValidFrom) {

		this.previousFileValidFrom = previousFileValidFrom;
	}


	public void addEntity(CPRRecord entity) {

		entity.setValidFrom(getValidFrom());

		if (entity instanceof Personoplysninger)
			personoplysninger.addRecord((Personoplysninger) entity);
		else if (entity instanceof Klarskriftadresse)
			klarskriftadresse.addRecord((Klarskriftadresse) entity);
		else if (entity instanceof NavneBeskyttelse)
			navneBeskyttelse.addRecord((NavneBeskyttelse) entity);
		else if (entity instanceof Navneoplysninger)
			navneoplysninger.addRecord((Navneoplysninger) entity);
		else if (entity instanceof UmyndiggoerelseVaergeRelation)
			umyndiggoerelseVaergeRelation.addRecord((UmyndiggoerelseVaergeRelation) entity);
		else if (entity instanceof ForaeldreMyndighedRelation)
			foraeldreMyndighedRelation.addRecord((ForaeldreMyndighedRelation) entity);
		else if (entity instanceof BarnRelation) barnRelation.addRecord((BarnRelation) entity);
	}


	public Dataset<Personoplysninger> getPersonoplysninger() {

		return personoplysninger;
	}


	public Dataset<Klarskriftadresse> getKlarskriftadresse() {

		return klarskriftadresse;
	}


	public Dataset<NavneBeskyttelse> getNavneBeskyttelse() {

		return navneBeskyttelse;
	}


	public Dataset<Navneoplysninger> getNavneoplysninger() {

		return navneoplysninger;
	}


	public Dataset<UmyndiggoerelseVaergeRelation> getUmyndiggoerelseVaergeRelation() {

		return umyndiggoerelseVaergeRelation;
	}


	public Dataset<ForaeldreMyndighedRelation> getForaeldreMyndighedRelation() {

		return foraeldreMyndighedRelation;
	}


	public Dataset<BarnRelation> getBarnRelation() {

		return barnRelation;
	}
}
