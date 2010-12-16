package com.trifork.sdm.models.takst;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.trifork.sdm.models.AbstractRecord;
import com.trifork.sdm.models.takst.unused.Beregningsregler;
import com.trifork.sdm.models.takst.unused.DivEnheder;
import com.trifork.sdm.models.takst.unused.EmballagetypeKoder;
import com.trifork.sdm.models.takst.unused.Enhedspriser;
import com.trifork.sdm.models.takst.unused.Firma;
import com.trifork.sdm.models.takst.unused.Indholdsstoffer;
import com.trifork.sdm.models.takst.unused.Laegemiddelnavn;
import com.trifork.sdm.models.takst.unused.Opbevaringsbetingelser;
import com.trifork.sdm.models.takst.unused.OplysningerOmDosisdispensering;
import com.trifork.sdm.models.takst.unused.Pakningskombinationer;
import com.trifork.sdm.models.takst.unused.PakningskombinationerUdenPriser;
import com.trifork.sdm.models.takst.unused.Priser;
import com.trifork.sdm.models.takst.unused.Rekommandationer;
import com.trifork.sdm.models.takst.unused.SpecialeForNBS;
import com.trifork.sdm.models.takst.unused.Substitution;
import com.trifork.sdm.models.takst.unused.SubstitutionAfLaegemidlerUdenFastPris;
import com.trifork.sdm.models.takst.unused.Tilskudsintervaller;
import com.trifork.sdm.models.takst.unused.TilskudsprisgrupperPakningsniveau;
import com.trifork.sdm.models.takst.unused.UdgaaedeNavne;
import com.trifork.sdm.models.takst.unused.Udleveringsbestemmelser;


@Entity
@Table(name = "TakstVersion")
public class Takst extends AbstractRecord {

	// Mandatory Content:
	//
	// The fields below are mandatory and thus always part of a release.

	public Set<Laegemiddel> drugs;
	public Set<Pakning> packaging;
	public Set<Priser> prices;
	public Set<Substitution> substitutions;
	public Set<SubstitutionAfLaegemidlerUdenFastPris> substitutionAfLaegemidlerUdenFastPris;
	public Set<TilskudsprisgrupperPakningsniveau> tilskudsprisgrupperPakningsniveau;
	public Set<Firma> firmaer = new HashSet<Firma>();
	public Set<UdgaaedeNavne> udgaaedeNavne;
	public Set<Administrationsvej> administrationsveje;
	public Set<ATCKoderOgTekst> atcKoderOgTekster;
	public Set<Beregningsregler> beregningsregler;
	public Set<EmballagetypeKoder> emballagetypeKoder;
	public Set<DivEnheder> divEnheder;
	public Set<Medicintilskud> medicintilskud;
	public Set<Klausulering> klausulering;
	public Set<Udleveringsbestemmelser> udleveringsbestemmelser;
	public Set<SpecialeForNBS> specialeForNBS;
	public Set<Opbevaringsbetingelser> opbevaringsbetingelser;
	public Set<Tilskudsintervaller> tilskudsintervaller;
	public Set<OplysningerOmDosisdispensering> oplysningerOmDosisdispensering;
	public Set<Indikationskode> indikationskoder;
	public Set<Indikation> indikation;
	public Set<Doseringskode> doseringskode;
	public Set<Dosering> dosering;

	// Optional Content:
	//
	// The fields below are not necessarily part of a release.

	public Set<Laegemiddelnavn> laegemiddelnavne;
	public Set<LaegemiddelformBetegnelser> laegemiddelformBetegnelser;
	public Set<Rekommandationer> rekommandationer;
	public Set<Indholdsstoffer> indholdsstoffer;
	public Set<Enhedspriser> enhedspriser;
	public Set<Pakningskombinationer> pakningskombinationer;
	public Set<PakningskombinationerUdenPriser> pakningskombinationerUdenPriser;

	// Inferred Data:

	public Set<Tidsenhed> tidsenheder;
	public Set<Pakningsstoerrelsesenhed> pakningsstoerrelsesenheder;
	public Set<Styrkeenhed> styrkeenheder;
	public Set<LaegemiddelAdministrationsvejRef> laegemiddelAdministrationsvejRef;

	// The week number:
	//
	// LMS guarantees some sort of stability/validity
	// for a subset of this release.
	// The stable subset excludes pricing and substitutions though possibly
	// more.

	private int validityYear;
	private int validityWeekNumber;


	public Takst(Date validFrom, Date validTo) {

		setValidFrom(validFrom);
		setValidTo(validTo);
	}


	@Column(name = "TakstUge")
	public String getStableWeek() {

		return "" + validityYear + validityWeekNumber;
	}


	public void setValidityYear(int validityYear) {

		this.validityYear = validityYear;
	}


	public void setValidityWeekNumber(int validityWeekNumber) {

		this.validityWeekNumber = validityWeekNumber;
	}


	/**
	 * This method is overridden since we need to add the {@code Id} annotation
	 * to it.
	 */
	@Id
	@Override
	public Date getValidFrom() {

		return super.getValidFrom();
	}
}
