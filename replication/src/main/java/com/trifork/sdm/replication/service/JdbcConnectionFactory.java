package com.trifork.sdm.replication.service;

import java.sql.Connection;

public interface JdbcConnectionFactory {

	Connection create();
}
