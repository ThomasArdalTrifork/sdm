package com.trifork.sdm.replication;

import java.sql.Connection;

public interface ConnectionFactory {

	Connection create();
}
