package com.trifork.sdm.replication;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Iterator;

import javax.inject.Inject;

import com.google.inject.assistedinject.Assisted;
import com.trifork.sdm.models.EntityHelper;
import com.trifork.sdm.models.Record;


public class MySQLQuery implements Query {

	private final long pid;
	private final Date date;
	private final Class<? extends Record> entity;
	private final ConnectionFactory connectionFactory;


	@Inject
	MySQLQuery(@Assisted Class<? extends Record> entity, @Assisted long pid, @Assisted Date since,
			ConnectionFactory connectionFactory) {

		this.pid = pid;
		this.date = since;
		this.entity = entity;
		this.connectionFactory = connectionFactory;
	}


	public long getPID() {

		return pid;
	}


	public Date getDate() {

		return date;
	}


	@Override
	public Iterator<Record> iterator() {

		Connection connection = connectionFactory.create();

		PreparedStatement stm;
		final RecordExtractor extractor = new RecordExtractor(entity);

		String tableName = EntityHelper.getTableName(entity);

		try {
			StringBuilder sql = new StringBuilder();
			sql.append(String.format("SELECT * FROM %s ", tableName));
			sql.append(String.format("WHERE (%sPID > ? AND ModifiedDate = ?) OR (ModifiedDate > ?) ", tableName));
			sql.append(String.format("ORDER BY %sPID, ModifiedDate, CreatedDate", tableName));

			stm = connection.prepareStatement(sql.toString());

			stm.setLong(1, pid);
			stm.setTimestamp(2, new Timestamp(date.getTime()));
			stm.setTimestamp(3, new Timestamp(date.getTime()));
			
			System.out.println(stm.toString());

			final ResultSet resultSet = stm.executeQuery();

			return new Iterator<Record>() {

				private final ResultSet results = resultSet;


				@Override
				public boolean hasNext() {

					try {
						return results.next();
					}
					catch (SQLException e) {
						throw new RuntimeException(e);
					}
				}


				@Override
				public Record next() {

					try {
						return extractor.extract(results);
					}
					catch (Exception e) {
						throw new RuntimeException(e);
					}
				}


				@Override
				public void remove() {

					throw new UnsupportedOperationException();
				}
			};
		}
		catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
}