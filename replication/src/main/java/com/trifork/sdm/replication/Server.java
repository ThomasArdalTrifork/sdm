package com.trifork.sdm.replication;


public interface Server {
	
	void start(); // TODO: These should throw exceptions on errors.
	
	void stop();
}
