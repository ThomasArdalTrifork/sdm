package com.trifork.sdm.models.cpr;

import java.util.Calendar;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import com.trifork.sdm.util.DateUtils;


@Entity
public class ForaeldreMyndighedRelation extends CPREntity {

	public enum ForaeldreMyndighedRelationsType {
		mor, far, andenIndehaver1, andenIndehaver2
	}


	String typeKode;
	ForaeldreMyndighedRelationsType type;
	Date foraeldreMyndighedStartDato;
	String foraeldreMyndighedMarkering;
	Date foraeldreMyndighedSlettedato;
	String relationCpr; // Hvis relationstypen ikke er mor eller far.
	Date relationCprStartDato;


	@Id
	@Column
	public String getId() {

		if (type == ForaeldreMyndighedRelationsType.mor) {
			return getCpr() + "-mor";
		}
		else if (type == ForaeldreMyndighedRelationsType.far) {
			return getCpr() + "-far";
		}
		return getCpr() + "-" + relationCpr;
	}


	@Column
	@Override
	public String getCpr() {

		return super.getCpr();
	}


	@Column
	public String getTypeTekst() {

		if (type == null)
			return "Ukendt forældre myndigheds relation";
		else if (type == ForaeldreMyndighedRelationsType.mor)
			return "Mor";
		else if (type == ForaeldreMyndighedRelationsType.far)
			return "Far";
		else if (type == ForaeldreMyndighedRelationsType.andenIndehaver1)
			return "Anden indenhaver 1";
		else if (type == ForaeldreMyndighedRelationsType.andenIndehaver2) return "Anden indenhaver 1";
		return null;
	}


	@Column
	public String getTypeKode() {

		return typeKode;
	}


	public void setType(String type) {

		if (type.equals("0003"))
			this.type = ForaeldreMyndighedRelationsType.mor;
		else if (type.equals("0004"))
			this.type = ForaeldreMyndighedRelationsType.far;
		else if (type.equals("0005"))
			this.type = ForaeldreMyndighedRelationsType.andenIndehaver1;
		else if (type.equals("0006"))
			this.type = ForaeldreMyndighedRelationsType.andenIndehaver2;
		else
			this.type = null;

		this.typeKode = type;
	}


	public Date getForaeldreMyndighedStartDato() {

		return foraeldreMyndighedStartDato;
	}


	public void setForaeldreMyndighedStartDato(Date foraeldreMyndighedStartDato) {

		this.foraeldreMyndighedStartDato = foraeldreMyndighedStartDato;
	}


	public String getForaeldreMyndighedMarkering() {

		return foraeldreMyndighedMarkering;
	}


	public void setForaeldreMyndighedMarkering(String foraeldreMyndighedMarkering) {

		this.foraeldreMyndighedMarkering = foraeldreMyndighedMarkering;
	}


	public Date getForaeldreMyndighedSlettedato() {

		return foraeldreMyndighedSlettedato;
	}


	public void setForaeldreMyndighedSlettedato(Date foraeldreMyndighedSlettedato) {

		this.foraeldreMyndighedSlettedato = foraeldreMyndighedSlettedato;
	}


	@Column
	public String getRelationCpr() {

		return relationCpr;
	}


	public void setRelationCpr(String relationCpr) {

		this.relationCpr = relationCpr;
	}


	public Date getRelationCprStartDato() {

		return relationCprStartDato;
	}


	public void setRelationCprStartDato(Date relationCprStartDato) {

		this.relationCprStartDato = relationCprStartDato;
	}


	@Override
	public Calendar getValidFrom() {

		return (foraeldreMyndighedStartDato == null) ? super.getValidFrom() : DateUtils
				.toCalendar(foraeldreMyndighedStartDato);
	}


	@Override
	public Calendar getValidTo() {

		return (foraeldreMyndighedSlettedato == null) ? super.getValidTo() : DateUtils
				.toCalendar(foraeldreMyndighedSlettedato);
	}

}
