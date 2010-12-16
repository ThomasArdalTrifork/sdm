package com.trifork.sdm.models.takst;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import com.trifork.sdm.models.AbstractRecord;
import com.trifork.sdm.util.DateUtils;


@Entity
public class Pakning extends AbstractRecord {

	private Long drugid; // Ref. t. LMS01, felt 01
	private Long varenummer;
	private Long alfabetSekvensnr;
	private Long varenummerForDelpakning; // Udfyldes for multipakningen
	private Long antalDelpakninger; // Antal delpakn. i stor/multipakning
	private String pakningsstoerrelseKlartekst;
	private Long pakningsstoerrelseNumerisk; // Brutto
	private String pakningsstoerrelseEnhed; // Ref. t. LMS15, enhedstype 4
	private String emballagetype; // Ref. t. LMS14
	// private String udleveringsbestemmelse; // Ref. t. LMS18
	// private String udleveringSpeciale; // Ref. t. LMS19
	private String medicintilskudskode; // Ref. t. LMS16
	private String klausulForMedicintilskud; // Ref. t. LMS17
	private Long antalDDDPrPakning;
	private Long opbevaringstidNumerisk; // Hos distributør
	// private String opbevaringstidEnhed; // Ref. t. LMS15, enhedstype 1
	// private String opbevaringsbetingelser; // Ref. t. LMS20
	private Long oprettelsesdato; // Format: YYYYMMDD
	private Long datoForSenestePrisaendring; // Format: YYYYMMDD
	private Long udgaaetDato; // YYYYMMDD
	// private String beregningskodeAIPRegpris; // Ref. t. LMS13
	private String pakningOptagetITilskudsgruppe; // 2 muligh.: F eller blank
	private String faerdigfremstillingsgebyr; // 2 muligh.: B eller blank


	// private Long pakningsdistributoer; // Ref. t. LMS09

	@Column(name = "DrugID")
	public Long getDrugid() {

		return this.drugid;
	}


	public void setDrugid(Long drugid) {

		this.drugid = drugid;
	}


	@Id
	@Column
	public Long getVarenummer() {

		return this.varenummer;
	}


	public void setVarenummer(Long varenummer) {

		this.varenummer = varenummer;
	}


	public Long getAlfabetSekvensnr() {

		return this.alfabetSekvensnr;
	}


	public void setAlfabetSekvensnr(Long alfabetSekvensnr) {

		this.alfabetSekvensnr = alfabetSekvensnr;
	}


	@Column(name = "VarenummerDelpakning")
	public Long getVarenummerForDelpakning() {

		return this.varenummerForDelpakning;
	}


	public void setVarenummerForDelpakning(Long varenummerForDelpakning) {

		this.varenummerForDelpakning = varenummerForDelpakning;
	}


	public Long getAntalDelpakninger() {

		return this.antalDelpakninger;
	}


	public void setAntalDelpakninger(Long antalDelpakninger) {

		this.antalDelpakninger = antalDelpakninger;
	}


	@Column(name = "PakningsstoerrelseTekst")
	public String getPakningsstoerrelseKlartekst() {

		return this.pakningsstoerrelseKlartekst;
	}


	public void setPakningsstoerrelseKlartekst(String pakningsstoerrelseKlartekst) {

		this.pakningsstoerrelseKlartekst = pakningsstoerrelseKlartekst;
	}


	@Column(name = "PakningsstoerrelseNumerisk")
	public Double getPakningsstoerrelseNumerisk() {

		if (this.pakningsstoerrelseNumerisk == 0) {
			return null;
		}
		return this.pakningsstoerrelseNumerisk / 100.0;
	}


	public void setPakningsstoerrelseNumerisk(Long pakningsstoerrelseNumerisk) {

		this.pakningsstoerrelseNumerisk = pakningsstoerrelseNumerisk;
	}


	@Column(name = "Pakningsstoerrelsesenhed")
	public String getPakningsstorrelseEnhed() {

		if (this.pakningsstoerrelseNumerisk == 0) {
			return null;
		}
		return this.pakningsstoerrelseEnhed;
	}


	public void setPakningsstoerrelseEnhed(String pakningsstoerrelseEnhed) {

		this.pakningsstoerrelseEnhed = pakningsstoerrelseEnhed;
	}


	@Column(name = "EmballageTypeKode")
	public String getEmballagetype() {

		return emballagetype;
	}


	public void setEmballagetype(String emballagetype) {

		this.emballagetype = emballagetype;
	}


	@Column(name = "MedicintilskudsKode")
	public String getMedicintilskudskode() {

		return this.medicintilskudskode;
	}


	public void setMedicintilskudskode(String medicintilskudskode) {

		this.medicintilskudskode = medicintilskudskode;
	}


	@Column(name = "KlausuleringsKode")
	public String getKlausulForMedicintilskud() {

		return this.klausulForMedicintilskud;
	}


	public void setKlausulForMedicintilskud(String klausulForMedicintilskud) {

		this.klausulForMedicintilskud = klausulForMedicintilskud;
	}


	public Double getAntalDDDPrPakning() {

		return (this.antalDDDPrPakning) / 1000.0;
	}


	public void setAntalDDDPrPakning(Long antalDDDPrPakning) {

		this.antalDDDPrPakning = antalDDDPrPakning;
	}


	public Long getOpbevaringstidNumerisk() {

		return this.opbevaringstidNumerisk;
	}


	public void setOpbevaringstidNumerisk(Long opbevaringstidNumerisk) {

		this.opbevaringstidNumerisk = opbevaringstidNumerisk;
	}


	public String getOprettelsesdato() {

		return DateUtils.toISO8601date(this.oprettelsesdato);
	}


	public void setOprettelsesdato(Long oprettelsesdato) {

		this.oprettelsesdato = oprettelsesdato;
	}


	public String getDatoForSenestePrisaendring() {

		return DateUtils.toISO8601date(this.datoForSenestePrisaendring);
	}


	public void setDatoForSenestePrisaendring(Long datoForSenestePrisaendring) {

		this.datoForSenestePrisaendring = datoForSenestePrisaendring;
	}


	public String getUdgaaetDato() {

		return DateUtils.toISO8601date(this.udgaaetDato);
	}


	public void setUdgaaetDato(Long udgaaetDato) {

		this.udgaaetDato = udgaaetDato;
	}


	public boolean getPakningOptagetITilskudsgruppe() {

		return "F".equalsIgnoreCase(this.pakningOptagetITilskudsgruppe);
	}


	public void setPakningOptagetITilskudsgruppe(String pakningOptagetITilskudsgruppe) {

		this.pakningOptagetITilskudsgruppe = pakningOptagetITilskudsgruppe;
	}


	public boolean getFaerdigfremstillingsgebyr() {

		return "B".equalsIgnoreCase(this.faerdigfremstillingsgebyr);
	}


	public void setFaerdigfremstillingsgebyr(String faerdigfremstillingsgebyr) {

		this.faerdigfremstillingsgebyr = faerdigfremstillingsgebyr;
	}


	@Column
	public Integer getDosisdispenserbar() {

		return takst.getRecord(Laegemiddel.class, drugid).getEgnetTilDosisdispensering();
	}
}