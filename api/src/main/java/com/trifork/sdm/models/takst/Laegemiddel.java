package com.trifork.sdm.models.takst;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import org.apache.log4j.Logger;

import com.trifork.sdm.models.AbstractRecord;


@Entity
public class Laegemiddel extends AbstractRecord {

	private Long drugid;

	private String varetype; // Udfyldt med SP (Specialiteter)
	private String varedeltype; // Udfyldt med LM (lægemiddel, reg.)
	private String alfabetSekvensplads;
	private Long specNummer; // D.sp.nr. (decentrale) - Alm. nr (centrale)
	private String navn; // Evt. forkortet
	private String laegemiddelformTekst; // Evt. forkortet
	private String formKode; // Ref. t. LMS22, felt 01
	private String kodeForYderligereFormOplysn; // Feltet er tomt pt.
	private String styrkeKlarTekst;
	private Long styrkeNumerisk;
	private String styrkeEnhed; // Ref. t. LMS15, enhedstype 3
	private Long mTIndehaver; // Ref. t. LMS09
	private Long repraesentantDistributoer;

	// Ref. t. LMS12
	private String aTC;

	// 4 x 2 kar. (Ref. t. LMS11)
	// private String administrationsvej;

	// 2 muligh.: J eller blank
	private String trafikadvarsel;

	// 2 muligh.: G eller blank
	private String substitution;

	// Substitutionsgruppenr. på Drugid-niveau
	private String laegemidletsSubstitutionsgruppe;

	// 2 choices: D eller blank
	private String egnetTilDosisdispensering;

	// Format: YYYYMMDD
	private String datoForAfregistrAfLaegemiddel;

	// Format: YYYYMMDD
	private String karantaenedato;

	private final Logger logger = Logger.getLogger(getClass());

	private String formBetegnelse;
	private String atcTekst;

	private Set<String> administrationCodes = new HashSet<String>();


	@Id
	@Column(name = "DrugID")
	public Long getDrugid() {

		return this.drugid;
	}


	public void setDrugid(Long drugid) {

		this.drugid = drugid;
	}


	public String getVaretype() {

		return this.varetype;
	}


	public void setVaretype(String varetype) {

		this.varetype = varetype;
	}


	public String getVaredeltype() {

		return this.varedeltype;
	}


	public void setVaredeltype(String varedeltype) {

		this.varedeltype = varedeltype;
	}


	public String getAlfabetSekvensplads() {

		return this.alfabetSekvensplads;
	}


	public void setAlfabetSekvensplads(String alfabetSekvensplads) {

		this.alfabetSekvensplads = alfabetSekvensplads;
	}


	public Long getSpecNummer() {

		return this.specNummer;
	}


	public void setSpecNummer(Long specNummer) {

		this.specNummer = specNummer;
	}


	@Column(name = "DrugName")
	public String getNavn() {

		if (this.navn == null || this.navn.trim().equals("")) {
			return "Ikke angivet";
		}
		return this.navn;
	}


	public void setNavn(String navn) {

		this.navn = navn;
	}


	public String getLaegemiddelformTekst() {

		return this.laegemiddelformTekst;
	}


	public void setLaegemiddelformTekst(String laegemiddelformTekst) {

		this.laegemiddelformTekst = laegemiddelformTekst;
	}


	@Column(name = "FormKode")
	public String getFormKode() {

		return this.formKode;
	}


	public void setFormKode(String formKode) {

		this.formKode = formKode;
	}


	public String getKodeForYderligereFormOplysn() {

		return this.kodeForYderligereFormOplysn;
	}


	public void setKodeForYderligereFormOplysn(String kodeForYderligereFormOplysn) {

		this.kodeForYderligereFormOplysn = kodeForYderligereFormOplysn;
	}


	@Column(name = "StyrkeTekst")
	public String getStyrkeKlarTekst() {

		return this.styrkeKlarTekst;
	}


	public void setStyrkeKlarTekst(String styrkeKlarTekst) {

		this.styrkeKlarTekst = styrkeKlarTekst;
	}


	@Column(name = "StyrkeNumerisk")
	public Double getStyrkeNumerisk() {

		if (styrkeNumerisk == null || styrkeNumerisk == 0) {
			return null;
		}
		return this.styrkeNumerisk / 1000.0;
	}


	public void setStyrkeNumerisk(Long styrkeNumerisk) {

		this.styrkeNumerisk = styrkeNumerisk;
	}


	@Column(name = "StyrkeEnhed")
	public String getStyrke() {

		if (styrkeNumerisk == null || styrkeNumerisk == 0) {
			return null;
		}
		return styrkeEnhed;
	}


	public void setStyrkeEnhed(String styrkeEnhed) {

		this.styrkeEnhed = styrkeEnhed;
	}


	public void setMTIndehaver(Long mTIndehaver) {

		this.mTIndehaver = mTIndehaver;
	}


	public void setRepraesentantDistributoer(Long repraesentantDistributoer) {

		this.repraesentantDistributoer = repraesentantDistributoer;
	}


	public void setATC(String aTC) {

		this.aTC = aTC;
	}


	public void addAdministrationCode(String code) {

		administrationCodes.add(code);
	}


	public Set<String> getAdministrationCodes() {

		return administrationCodes;
	}


	public Boolean getTrafikadvarsel() {

		return "J".equalsIgnoreCase(this.trafikadvarsel);
	}


	public void setTrafikadvarsel(String trafikadvarsel) {

		this.trafikadvarsel = trafikadvarsel;
	}


	public String getSubstitution() {

		return this.substitution;
	}


	public void setSubstitution(String substitution) {

		this.substitution = substitution;
	}


	public String getLaegemidletsSubstitutionsgruppe() {

		return this.laegemidletsSubstitutionsgruppe;
	}


	public void setLaegemidletsSubstitutionsgruppe(String laegemidletsSubstitutionsgruppe) {

		this.laegemidletsSubstitutionsgruppe = laegemidletsSubstitutionsgruppe;
	}


	@Column(name = "Dosisdispenserbar")
	public Integer getEgnetTilDosisdispensering() {

		return ("D".equals(this.egnetTilDosisdispensering)) ? 1 : 0;
	}


	public void setEgnetTilDosisdispensering(String egnetTilDosisdispensering) {

		this.egnetTilDosisdispensering = egnetTilDosisdispensering;
	}


	public String getDatoForAfregistrAfLaegemiddel() {

		if (datoForAfregistrAfLaegemiddel == null || datoForAfregistrAfLaegemiddel.isEmpty()) {

			return null;
		}

		SimpleDateFormat informat = new SimpleDateFormat("yyyyMMdd");
		SimpleDateFormat outformat = new SimpleDateFormat("yyyy-MM-dd");

		try {
			return outformat.format(informat.parse(this.datoForAfregistrAfLaegemiddel));
		}
		catch (ParseException e) {
			logger.error("Error converting DatoForAfregistrAfLaegemiddel to iso 8601 date format. Returning unformated string: '" + this.datoForAfregistrAfLaegemiddel + "'");
			return this.datoForAfregistrAfLaegemiddel;
		}
	}


	public void setDatoForAfregistrAfLaegemiddel(String datoForAfregistrAfLaegemiddel) {

		this.datoForAfregistrAfLaegemiddel = datoForAfregistrAfLaegemiddel;
	}


	public String getKarantaenedato() {

		if (this.karantaenedato == null || "".equals(this.karantaenedato)) return null;
		SimpleDateFormat informat = new SimpleDateFormat("yyyyMMdd");
		SimpleDateFormat outformat = new SimpleDateFormat("yyyy-MM-dd");
		try {
			return outformat.format(informat.parse(this.datoForAfregistrAfLaegemiddel));
		}
		catch (ParseException e) {
			logger.error("Error converting DatoForAfregistrAfLaegemiddel to iso 8601 date format. Returning unformated string");
			return this.datoForAfregistrAfLaegemiddel;
		}
	}


	public void setKarantaenedato(String karantaenedato) {

		this.karantaenedato = karantaenedato;
	}


	public void setForm(String form) {

		this.formBetegnelse = form;
	}


	@Column(name = "FormTekst")
	public String getForm() {

		return formBetegnelse;
	}


	@Column(name = "ATCKode")
	public String getATC() {

		return aTC;
	}


	public void setATCTekst(String atcTekst) {

		this.atcTekst = atcTekst;
	}


	@Column(name = "ATCTekst")
	public String getATCTekst() {

		return atcTekst;
	}


	public Object getMTIndehaverKode() {

		return mTIndehaver;
	}


	public Object getRepraesentantDistributoerKode() {

		return repraesentantDistributoer;
	}


	public Boolean getEksperimentieltLaegemiddel() {

		return ("" + drugid).startsWith("2742");
	}


	public Boolean getMagistreltLaegemiddel() {

		return ("" + drugid).startsWith("8");
	}


	public Boolean isTilHumanAnvendelse() {

		if (aTC == null) return null;

		return !aTC.startsWith("Q");
	}
}