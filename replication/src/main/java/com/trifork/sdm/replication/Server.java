package com.trifork.sdm.replication;


public interface Server extends Runnable {
	
	/**
	 * Runs the server.
	 * 
	 * The server will block until stopped.
	 */
	void run(); // TODO: These should throw exceptions on errors.
	
	/**
	 * Stops the server.
	 * 
	 * This has no effect if the server is not running.
	 */
	void stop();
}
