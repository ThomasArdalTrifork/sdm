package com.trifork.sdm.replication.persistence;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map.Entry;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.trifork.sdm.models.EntityHelper;
import com.trifork.sdm.replication.ConnectionManager;
import com.trifork.sdm.replication.EntityWriter;
import com.trifork.sdm.replication.UpdateQueryBuilder.UpdateQuery;


public class EntityRepository {

	private final String selectSQL;
	private final ConnectionManager connectionManager;

	private Class<?> entity;
	private HashMap<String, Method> properties;


	public EntityRepository(Class<?> entity, ConnectionManager connectionManager) {

		Entity typeAnnotation = entity.getAnnotation(Entity.class);
		assert typeAnnotation != null;

		this.connectionManager = connectionManager;
		assert connectionManager != null;

		String tableName;
		
		Table table = entity.getAnnotation(Table.class);
		
		if (table != null && !table.name().isEmpty())
			tableName = table.name();
		else
			tableName = entity.getSimpleName();

		// TODO: Clean this up.
		
		selectSQL = "SELECT * FROM " + tableName + " " +
					"WHERE (ModifiedDate > ?) OR (ModifiedDate = ? AND "+tableName+"PID > ?) " +
					"ORDER BY " + tableName + "PID, ModifiedDate, CreatedDate LIMIT ?";

		properties = new HashMap<String, Method>();
		for (Method getter : entity.getMethods()) {

			if (!getter.isAnnotationPresent(Column.class)) continue;

			String columnName = EntityHelper.getPropetyName(getter);
			Method setter = EntityHelper.getSetterFromGetter(entity, getter);

			properties.put(columnName, setter);
		}
	}


	public void writeAll(UpdateQuery query, long limit, EntityWriter writer, OutputStream outputStream)
			throws IOException {

		assert limit > 0;
		assert writer != null;

		Connection connection = null;

		try {
			connection = connectionManager.getConnection();

			PreparedStatement selectStm = connection.prepareStatement(selectSQL);

			// Fill in the since parameter.
			java.sql.Date sqlSince = new java.sql.Date(query.getDate().getTime());
			selectStm.setDate(1, sqlSince);
			selectStm.setDate(2, sqlSince);
			selectStm.setLong(3, query.getPID());
			selectStm.setLong(4, limit);

			// Fetch the records.
			ResultSet results = selectStm.executeQuery();

			while (results.next()) {

				Object record;

				try {
					record = entity.newInstance();
				}
				catch (Exception e) {
					throw new RuntimeException(String.format(
							"Entity '%s' did not have a default constructor.", entity.getSimpleName()), e);
				}

				// For each output property fetch the corresponding data.
				for (Entry<String, Method> property : properties.entrySet()) {

					String column = property.getKey();
					Method setter = property.getValue();

					try {
						Class<?> type = setter.getParameterTypes()[0];

						if (type == String.class) {
							setter.invoke(record, results.getString(column));
						}
						else if (type == Date.class) {
							Date date = results.getDate(column);
							setter.invoke(record, date);
						}
						else if (type == Integer.class || type == int.class) {
							setter.invoke(record, results.getInt(column));
						}
						else if (type == Long.class || type == long.class) {
							setter.invoke(record, results.getLong(column));
						}
					}
					catch (Exception e) {
						throw new RuntimeException(String.format(
								"Error while populating a property on entity '%s'.", entity.getSimpleName()),
								e);
					}
				}

				writer.write(record, outputStream);
				outputStream.flush();
			}
		}
		catch (SQLException e) {

			// This should never happen. We cannot recover.
			throw new RuntimeException(e);
		}
		finally {
			try {
				connection.close();
			}
			catch (Throwable t) {
				// TODO: Refactor this it is ugly.
			}
		}
	}
}
