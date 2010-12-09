package com.trifork.sdm.importer.persistence.mysql;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import org.junit.Before;
import org.junit.Test;

import com.trifork.sdm.models.Record;
import com.trifork.sdm.persistence.CompleteDataset;
import com.trifork.sdm.util.DateUtils;


public class MySQLDaoIntegrationTest extends AbstractMySQLIntegationTest {

	@Before
	public void setupTable() throws SQLException {

		Connection con = MySQLConnectionManager.getAutoCommitConnection();
		Statement stmt = con.createStatement();

		try {
			stmt.executeUpdate("drop table SDE");
		}
		catch (Exception e) {}

		try {
			con.createStatement().executeUpdate(
					"create table SDE( " + "id VARCHAR(20) NOT NULL, " + "data VARCHAR(20),"
							+ "date DATETIME," + "				 ModifiedBy VARCHAR(200) NOT NULL,"
							+ "				 ModifiedDate DATETIME NOT NULL," + "				 ValidFrom DATETIME ,"
							+ "				 ValidTo DATETIME," + "				 CreatedBy VARCHAR(200),"
							+ "				 CreatedDate DATETIME);");
		}
		catch (Exception e) {
			// it probably already existed
		}
		stmt.close();
		con.close();
	}


	@Test
	public void testPersistCompleteDataset() throws Exception {

		CompleteDataset<SDE> dataset = new CompleteDataset<SDE>(SDE.class, t0, t1);
		dataset.addEntity(new SDE(t0, DateUtils.FUTURE));
		Connection con = MySQLConnectionManager.getAutoCommitConnection();
		MySQLTemporalDao dao = new MySQLTemporalDao(con);
		dao.persistCompleteDataset(dataset);
		MySQLTemporalTable<SDE> table = dao.getTable(SDE.class);
		assertTrue(table.fetchEntityVersions(t0, t1));
		assertEquals(table.getCurrentRowValidFrom(), t0);
		assertEquals(table.getCurrentRowValidTo(), DateUtils.FUTURE);
		assertFalse(table.nextRow());
		con.close();
	}


	@Test
	public void testPersistCompleteDatasetX2() throws Exception {

		CompleteDataset<SDE> dataset = new CompleteDataset<SDE>(SDE.class, t0, t1);
		dataset.addEntity(new SDE(t0, DateUtils.FUTURE));
		Connection con = MySQLConnectionManager.getAutoCommitConnection();
		MySQLTemporalDao dao = new MySQLTemporalDao(con);
		dao.persistCompleteDataset(dataset);
		dao.persistCompleteDataset(dataset);
		MySQLTemporalTable table = dao.getTable(SDE.class);
		assertTrue(table.fetchEntityVersions(t0, t1));
		assertEquals(table.getCurrentRowValidFrom(), t0);
		assertEquals(table.getCurrentRowValidTo(), DateUtils.FUTURE);
		assertFalse(table.nextRow());
		con.close();
	}


	@Test
	public void testPersistCompleteDatasetChangedStringSameValidity() throws Exception {

		CompleteDataset<SDE> dataset1 = new CompleteDataset<SDE>(SDE.class, t0, t1);
		CompleteDataset<SDE> dataset2 = new CompleteDataset<SDE>(SDE.class, t0, t1);
		dataset1.addEntity(new SDE(t0, DateUtils.FUTURE, "1", "a"));
		dataset2.addEntity(new SDE(t0, DateUtils.FUTURE, "1", "b"));
		Connection con = MySQLConnectionManager.getAutoCommitConnection();
		MySQLTemporalDao dao = new MySQLTemporalDao(con);
		dao.persistCompleteDataset(dataset1);
		dao.persistCompleteDataset(dataset2);
		MySQLTemporalTable table = dao.getTable(SDE.class);
		assertTrue(table.fetchEntityVersions(t0, t1));
		assertEquals(table.getCurrentRowValidFrom(), t0);
		assertEquals(DateUtils.FUTURE, table.getCurrentRowValidTo());
		assertEquals("b", table.currentRS.getString("data"));
		assertFalse(table.nextRow());
		con.close();
	}


	@Test
	public void testPersistCompleteDatasetChangedDateSameValidity() throws Exception {

		CompleteDataset<SDE> dataset1 = new CompleteDataset<SDE>(SDE.class, t0, t1);
		CompleteDataset<SDE> dataset2 = new CompleteDataset<SDE>(SDE.class, t0, t1);
		dataset1.addEntity(new SDE(t0, DateUtils.FUTURE, "1", "a", t3.getTime()));
		dataset2.addEntity(new SDE(t0, DateUtils.FUTURE, "1", "a", t4.getTime()));
		Connection con = MySQLConnectionManager.getAutoCommitConnection();
		MySQLTemporalDao dao = new MySQLTemporalDao(con);
		dao.persistCompleteDataset(dataset1);
		dao.persistCompleteDataset(dataset2);
		MySQLTemporalTable table = dao.getTable(SDE.class);
		assertTrue(table.fetchEntityVersions(t0, t1));
		assertEquals(table.getCurrentRowValidFrom(), t0);
		assertEquals(DateUtils.FUTURE, table.getCurrentRowValidTo());
		assertEquals(t4.getTime().getTime(), table.currentRS.getTimestamp("date").getTime());
		assertFalse(table.nextRow());
		con.close();
	}


	@Test
	public void testPersistCompleteDatasetChangedDataNewValidFrom() throws Exception {

		CompleteDataset<SDE> dataset1 = new CompleteDataset<SDE>(SDE.class, t0, t1);
		CompleteDataset<SDE> dataset2 = new CompleteDataset<SDE>(SDE.class, t1, t2);
		dataset1.addEntity(new SDE(t0, DateUtils.FUTURE, "1", "a"));
		dataset2.addEntity(new SDE(t1, DateUtils.FUTURE, "1", "b"));
		Connection con = MySQLConnectionManager.getAutoCommitConnection();
		MySQLTemporalDao dao = new MySQLTemporalDao(con);
		dao.persistCompleteDataset(dataset1);
		dao.persistCompleteDataset(dataset2);
		MySQLTemporalTable table = dao.getTable(SDE.class);
		assertTrue(table.fetchEntityVersions(t0, t0)); // Get the old version
		assertEquals(t0, table.getCurrentRowValidFrom());
		assertEquals(t1, table.getCurrentRowValidTo());
		assertEquals("a", table.currentRS.getString("data"));
		assertFalse(table.nextRow());
		assertTrue(table.fetchEntityVersions(t2, t2)); // Get the new version
		assertEquals(table.getCurrentRowValidFrom(), t1);
		assertEquals(DateUtils.FUTURE, table.getCurrentRowValidTo());
		assertEquals("b", table.currentRS.getString("data"));
		assertFalse(table.nextRow());
		con.close();
	}


	@Test
	public void testPersistCompleteDatasetChangedDataNewValidToNoDataChange() throws Exception {

		CompleteDataset<SDE> dataset1 = new CompleteDataset<SDE>(SDE.class, t0, t1);
		CompleteDataset<SDE> dataset2 = new CompleteDataset<SDE>(SDE.class, t1, t2);
		CompleteDataset<SDE> dataset3 = new CompleteDataset<SDE>(SDE.class, t2, t1000);
		dataset1.addEntity(new SDE(t0, DateUtils.FUTURE, "1", "a")); // Normal
																		// t0 ->
																		// FUTURE
		dataset2.addEntity(new SDE(t0, t1, "1", "a")); // Limit validTo to T1 no
														// data change
		dataset3.addEntity(new SDE(t0, t1000, "1", "a")); // Extend validTo to
															// after FUTURE
		Connection con = MySQLConnectionManager.getAutoCommitConnection();
		MySQLTemporalDao dao = new MySQLTemporalDao(con);
		dao.persistCompleteDataset(dataset1);
		MySQLTemporalTable table = dao.getTable(SDE.class);
		assertTrue(table.fetchEntityVersions(t0, DateUtils.FUTURE));
		assertEquals(t0, table.getCurrentRowValidFrom());
		assertEquals(DateUtils.FUTURE, table.getCurrentRowValidTo());
		assertEquals("a", table.currentRS.getString("data"));
		assertFalse(table.nextRow());

		// Persist validTo = T1
		dao.persistCompleteDataset(dataset2);
		assertTrue(table.fetchEntityVersions(t0, DateUtils.FUTURE));
		assertEquals(t0, table.getCurrentRowValidFrom());
		assertEquals(t1, table.getCurrentRowValidTo());
		assertEquals("a", table.currentRS.getString("data"));
		assertFalse(table.nextRow());

		// Persist validTo = T1000
		dao.persistCompleteDataset(dataset3);
		assertTrue(table.fetchEntityVersions(t0, DateUtils.FUTURE));
		assertEquals(t0, table.getCurrentRowValidFrom());
		assertEquals(t1000, table.getCurrentRowValidTo());
		assertEquals("a", table.currentRS.getString("data"));
		assertFalse(table.nextRow());
		con.close();
	}


	@Entity
	private static class SDE implements Record {

		Calendar validfrom, validto;
		String id = "1"; // default value
		String data = "a"; // default value
		Date date = DateUtils.toCalendar(2001, 1, 1, 1, 2, 3).getTime();


		public SDE(Calendar validFrom, Calendar validTo) {

			this.validfrom = validFrom;
			this.validto = validTo;
		}


		public SDE(Calendar validFrom, Calendar validTo, String id, String data) {

			this.validfrom = validFrom;
			this.validto = validTo;
			this.data = data;
			this.id = id;
		}


		public SDE(Calendar validFrom, Calendar validTo, String id, String data, Date date) {

			this.validfrom = validFrom;
			this.validto = validTo;
			this.data = data;
			this.id = id;
			this.date = date;
		}


		public Calendar getValidFrom() {

			// TODO Auto-generated method stub
			return validfrom;
		}


		public Calendar getValidTo() {

			// TODO Auto-generated method stub
			return validto;
		}


		public Map<String, Object> serialize() {

			// TODO Auto-generated method stub
			return null;
		}


		@Column
		public String getData() {

			return data;
		}


		@Id
		@Column(name = "id")
		public Object getRecordId() {

			// TODO Auto-generated method stub
			return id;
		}


		@Column
		public Date getDate() {

			return date;
		}
	}
}
