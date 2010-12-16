package com.trifork.sdm.importer.persistence.mysql;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.trifork.sdm.importer.persistence.mysql.MySQLTemporalTable.RecordVersion;
import com.trifork.sdm.models.Record;
import com.trifork.sdm.models.takst.Laegemiddel;
import com.trifork.sdm.models.takst.Takst;
import com.trifork.sdm.models.takst.TakstDataset;
import com.trifork.sdm.models.takst.unused.DivEnheder;
import com.trifork.sdm.util.DateUtils;


public class MySQLStamDAOTest {

	private Takst takst;
	private Laegemiddel laegemiddel;
	private MySQLTemporalDao dao;

	private MySQLTemporalTable<Record> laegemiddeltableMock;


	@Before
	public void setUp() throws Exception {

		takst = new Takst(DateUtils.toDate(2009, 7, 1), DateUtils.toDate(2009, 7, 14));

		// Add a dataset to the takst with one member

		List<Laegemiddel> list = new ArrayList<Laegemiddel>();

		laegemiddel = new Laegemiddel();
		laegemiddel.setDrugid(1l);
		laegemiddel.setNavn("Zymedolinatexafylitungebraekker");

		list.add(laegemiddel);

		TakstDataset<Laegemiddel> dataset = new TakstDataset<Laegemiddel>(takst, list, Laegemiddel.class);

		takst.addRecordSet(dataset);

		/*
		 * Add an empty dataset to the takst (should be ignored)
		 * List<Pakning> tomListe = new ArrayList<Pakning>(); Dataset<Pakning>
		 * tomtDataset = new Dataset<Pakning>(takst, tomListe, Pakning.class);
		 * takst.addDataset(tomtDataset);
		 */
		
		// Add a dataset to the takst, which should be ignored because it is not
		List<DivEnheder> enheder = new ArrayList<DivEnheder>();
		DivEnheder enhed = new DivEnheder();
		
		enhed.setTekst("millimol pr. gigajoule");
		
		TakstDataset<DivEnheder> hiddenDataset = new TakstDataset<DivEnheder>(takst, enheder,
				DivEnheder.class);
		takst.addRecordSet(hiddenDataset);

		// Setup database mocking.

		Connection con = mock(Connection.class);

		MySQLTemporalDao realDao = new MySQLTemporalDao(con);

		dao = spy(realDao);

		laegemiddeltableMock = mock(MySQLTemporalTable.class);

		doReturn(laegemiddeltableMock).when(dao).getTable(Laegemiddel.class);
	}


	@Test
	public void testPersistOneLaegemiddel() throws Exception {

		when(laegemiddeltableMock.fetchEntityVersions(anyObject(), any(Date.class), any(Date.class)))
				.thenReturn(false);
		
		// Simulate no existing entities.

		dao.persistCompleteDatasets(takst.getDatasets());

		// Verify that the new record is inserted.
		verify(laegemiddeltableMock, times(1)).insertRow(eq(laegemiddel), any(Date.class));
	}


	@Test
	public void testDeltaPutChanged() throws Exception {

		// Simulate that the entity is already present.
		when(laegemiddeltableMock.fetchEntityVersions(anyObject(), any(Date.class), any(Date.class)))
				.thenReturn(true);

		// Simulate that the existing row's validity range is 1950 to infinity.
		// So it must be updated.
		when(laegemiddeltableMock.getCurrentRowValidFrom()).thenReturn(DateUtils.toDate(1950, 01, 1));
		when(laegemiddeltableMock.getCurrentRowValidTo()).thenReturn(DateUtils.FUTURE);

		// Simulate that the entity has changed.
		when(laegemiddeltableMock.dataInCurrentRowEquals(any(Record.class))).thenReturn(false);

		dao.persistCompleteDatasets(takst.getDatasets());

		// Verify that the new record is inserted
		verify(laegemiddeltableMock, times(1)).insertAndUpdateRow(eq(laegemiddel), any(Date.class));

		// Verify that the existing record is updated
		verify(laegemiddeltableMock, times(1)).updateValidToOnCurrentRow(eq(takst.getValidFrom()),
				any(Date.class));
	}


	@Test
	public void testDeltaPutUnchanged() throws Exception {

		// Simulate that the entity is already present.
		when(laegemiddeltableMock.fetchEntityVersions(anyObject(), any(Date.class), any(Date.class)))
				.thenReturn(true);

		// Simulate that the existing row's validity range is 1950 to infinity.
		when(laegemiddeltableMock.getCurrentRowValidFrom()).thenReturn(DateUtils.toDate(1950, 01, 1));
		when(laegemiddeltableMock.getCurrentRowValidTo()).thenReturn(DateUtils.FUTURE);

		// Simulate that the entity is unchanged.
		when(laegemiddeltableMock.dataInCurrentRowEquals(any(Record.class))).thenReturn(true);

		dao.persistCompleteDatasets(takst.getDatasets());

		// Verify that the new record is inserted
		verify(laegemiddeltableMock, times(0)).insertRow(eq(laegemiddel), any(Date.class));

		// Verify that the existing record is not updated
		verify(laegemiddeltableMock, times(0)).updateValidToOnCurrentRow(eq(takst.getValidFrom()),
				any(Date.class));
	}


	@Test
	public void testDeltaPutRemoved() throws Exception {

		// An empty takst
		takst = new Takst(DateUtils.toDate(2009, 7, 1), DateUtils.toDate(2009, 7, 14));
		// ..with an empty dataset
		TakstDataset<Laegemiddel> lmr = new TakstDataset<Laegemiddel>(takst, new ArrayList<Laegemiddel>(), Laegemiddel.class);
		takst.addRecordSet(lmr);

		List<RecordVersion> sev = new ArrayList<RecordVersion>();
		// Simulate that there is one record
		RecordVersion sv = new RecordVersion();
		sv.id = 1;

		// Simulate that the existing row's validity range is 1950 to infinity.
		sv.validFrom = DateUtils.toDate(1950, 01, 1);
		sev.add(sv);

		when(laegemiddeltableMock.getRecordVersions(any(Date.class), any(Date.class)))
				.thenReturn(sev);

		dao.persistCompleteDatasets(takst.getDatasets());

		// Verify that the existing record is updated
		verify(laegemiddeltableMock, times(1)).updateValidToOnEntityVersion(
				eq(DateUtils.toDate(2009, 7, 1)), any(RecordVersion.class), any(Date.class));

	}

}
