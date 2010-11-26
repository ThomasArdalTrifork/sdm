package com.trifork.sdm.importer.monitoring;

public interface FailureNotifier
{
	void notify(String message);
	
	void notify(String message, Exception cause);
}
