package com.trifork.sdm.models.cpr;

import java.util.Date;

import com.trifork.sdm.persistence.annotations.Id;
import com.trifork.sdm.persistence.annotations.Output;

@Output(name = "Person")
public class NavneBeskyttelse extends CPREntity {
	Date navneBeskyttelseStartDato;
	Date navneBeskyttelseSletteDato;

	@Id
	@Output
	@Override
	public String getCpr() {
		return super.getCpr();
	}

	@Output
	public Date getNavneBeskyttelseStartDato() {
		return navneBeskyttelseStartDato;
	}

	public void setNavneBeskyttelseStartDato(Date startDato) {
		this.navneBeskyttelseStartDato = startDato;
	}

	@Output
	public Date getNavneBeskyttelseSletteDato() {
		return navneBeskyttelseSletteDato;
	}

	public void setNavneBeskyttelseSletteDato(Date sletteDato) {
		this.navneBeskyttelseSletteDato = sletteDato;
	}
}
