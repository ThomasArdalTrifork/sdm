package com.trifork.sdm.models.cpr;

import java.util.Date;

import com.trifork.sdm.persistence.annotations.Id;
import com.trifork.sdm.persistence.annotations.Output;

@Output(name = "Person")
public class Personoplysninger extends CPREntity {
	String gaeldendeCpr;
	String status;
	Date statusDato;
	String statusMakering;
	String koen;
	Date foedselsdato;
	String foedselsdatoMarkering;
	Date startDato;
	String startDatoMarkering;
	Date slutDato;
	String slutDatoMarkering;
	String stilling;

	@Id
	@Output
	@Override
	public String getCpr() {
		return super.getCpr();
	}

	@Output
	public String getGaeldendeCpr() {
		return gaeldendeCpr;
	}

	public void setGaeldendeCpr(String gaeldendeCpr) {
		this.gaeldendeCpr = gaeldendeCpr;
	}

	@Output
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getStatusDato() {
		return statusDato;
	}

	public void setStatusDato(Date statusDato) {
		this.statusDato = statusDato;
	}

	public String getStatusMakering() {
		return statusMakering;
	}

	public void setStatusMakering(String statusMakering) {
		this.statusMakering = statusMakering;
	}

	@Output
	public String getKoen() {
		return koen;
	}

	public void setKoen(String koen) {
		this.koen = koen;
	}

	@Output
	public Date getFoedselsdato() {
		return foedselsdato;
	}

	public void setFoedselsdato(Date foedselsdato) {
		this.foedselsdato = foedselsdato;
	}

	public String getFoedselsdatoMarkering() {
		return foedselsdatoMarkering;
	}

	public void setFoedselsdatoMarkering(String foedselsdatoMarkering) {
		this.foedselsdatoMarkering = foedselsdatoMarkering;
	}

	public Date getStartDato() {
		return startDato;
	}

	public void setStartDato(Date startDato) {
		this.startDato = startDato;
	}

	public String getStartDatoMarkering() {
		return startDatoMarkering;
	}

	public void setStartDatoMarkering(String startDatoMarkering) {
		this.startDatoMarkering = startDatoMarkering;
	}

	public Date getSlutDato() {
		return slutDato;
	}

	public void setSlutdato(Date slutDato) {
		this.slutDato = slutDato;
	}

	public String getSlutDatoMarkering() {
		return slutDatoMarkering;
	}

	public void setSlutDatoMarkering(String slutDatoMarkering) {
		this.slutDatoMarkering = slutDatoMarkering;
	}

	@Output
	public String getStilling() {
		return stilling;
	}

	public void setStilling(String stilling) {
		this.stilling = stilling;
	}
}
