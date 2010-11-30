package com.trifork.sdm.replication;


public interface FailureNotifier {
	
	void notify(String message);
	
	void notify(String message, Throwable t);
}
