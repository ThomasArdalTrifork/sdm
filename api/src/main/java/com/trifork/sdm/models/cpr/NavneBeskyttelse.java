package com.trifork.sdm.models.cpr;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name = "Person")
public class NavneBeskyttelse extends CPREntity {

	Date navneBeskyttelseStartDato;
	Date navneBeskyttelseSletteDato;


	@Id
	@Column
	public String getCpr() {

		return super.getCpr();
	}


	@Column
	public Date getNavneBeskyttelseStartDato() {

		return navneBeskyttelseStartDato;
	}


	public void setNavneBeskyttelseStartDato(Date startDato) {

		this.navneBeskyttelseStartDato = startDato;
	}


	@Column
	public Date getNavneBeskyttelseSletteDato() {

		return navneBeskyttelseSletteDato;
	}


	public void setNavneBeskyttelseSletteDato(Date sletteDato) {

		this.navneBeskyttelseSletteDato = sletteDato;
	}
}
