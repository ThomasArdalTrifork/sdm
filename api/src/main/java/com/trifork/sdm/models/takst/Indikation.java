package com.trifork.sdm.models.takst;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name = "Indikation")
public class Indikation extends TakstEntity {

	private Long indikationskode; // Ref. t. LMS25
	private String indikationstekstTotal; // Felt 03 + 04 + 05
	private String indikationstekstLinie1;
	private String indikationstekstLinie2;
	private String indikationstekstLinie3;
	private String aktivInaktiv; // TODO: A = Aktiv kode. I = Inaktiv kode
									// (bliver pt. ikke anvendes)


	public List<ATCKoderOgTekst> getATC() {

		TakstDataset<Indikationskode> indikationskoder = takst.getDatasetOfType(Indikationskode.class);
		List<ATCKoderOgTekst> atcKoder = new ArrayList<ATCKoderOgTekst>();
		for (Indikationskode ik : indikationskoder.getEntities()) {
			if (ik.getIndikationskode().equals(this.getIndikationskode()))
				atcKoder.add(takst.getEntity(ATCKoderOgTekst.class, ik.getATC()));
		}
		return atcKoder;
	}


	@Id
	@Column(name = "IndikationKode")
	public Long getIndikationskode() {

		return this.indikationskode;
	}


	public void setIndikationskode(Long indikationskode) {

		this.indikationskode = indikationskode;
	}


	@Column(name = "IndikationTekst")
	public String getIndikationstekstTotal() {

		return this.indikationstekstTotal;
	}


	public void setIndikationstekstTotal(String indikationstekstTotal) {

		this.indikationstekstTotal = indikationstekstTotal;
	}


	// @Column
	public String getIndikationstekstLinie1() {

		return this.indikationstekstLinie1;
	}


	public void setIndikationstekstLinie1(String indikationstekstLinie1) {

		this.indikationstekstLinie1 = indikationstekstLinie1;
	}


	// @Column
	public String getIndikationstekstLinie2() {

		return this.indikationstekstLinie2;
	}


	public void setIndikationstekstLinie2(String indikationstekstLinie2) {

		this.indikationstekstLinie2 = indikationstekstLinie2;
	}


	// @Column
	public String getIndikationstekstLinie3() {

		return this.indikationstekstLinie3;
	}


	public void setIndikationstekstLinie3(String indikationstekstLinie3) {

		this.indikationstekstLinie3 = indikationstekstLinie3;
	}


	// @Column(name="aktiv")
	public Boolean getAktivInaktiv() {

		return "A".equalsIgnoreCase(this.aktivInaktiv);
	}


	public void setAktivInaktiv(String aktivInaktiv) {

		this.aktivInaktiv = aktivInaktiv;
	}


	public Long getEntityId() {

		return this.indikationskode;
	}

}