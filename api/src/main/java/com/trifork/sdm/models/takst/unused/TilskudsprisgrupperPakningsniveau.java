package com.trifork.sdm.models.takst.unused;

import com.trifork.sdm.models.AbstractRecord;

public class TilskudsprisgrupperPakningsniveau extends AbstractRecord {
	
	private Long tilskudsprisGruppe;
	private Long varenummer; // Ref. t. LMS02


	public Long getTilskudsprisGruppe() {

		return this.tilskudsprisGruppe;
	}


	public void setTilskudsprisGruppe(Long tilskudsprisGruppe) {

		this.tilskudsprisGruppe = tilskudsprisGruppe;
	}


	public Long getVarenummer() {

		return this.varenummer;
	}


	public void setVarenummer(Long varenummer) {

		this.varenummer = varenummer;
	}


	@Override
	public Long getKey() {

		return varenummer;
	}

}