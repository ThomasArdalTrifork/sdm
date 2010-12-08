package com.trifork.sdm.replication;

import java.sql.Connection;

public interface ConnectionManager {

	Connection getConnection();

	void close(Connection connection);
}
