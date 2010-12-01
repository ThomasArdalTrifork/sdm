package com.trifork.sdm.replication;


public interface FailureNotifier {
	
	void fatal(String string, Throwable t);

	void error(String string, Throwable t);

	void warn(String errorMessage);
}
