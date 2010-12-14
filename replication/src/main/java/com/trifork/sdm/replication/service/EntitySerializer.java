package com.trifork.sdm.replication.service;

import java.io.IOException;
import java.io.OutputStream;


/**
 * Writes entity instances to an output stream.
 * 
 * Implementing classes should support a single output format e.g. XML or
 * FastInfoset.
 */
public interface EntitySerializer {

	/**
	 * Implementing classes must return the HTTP content type for the generated output,
	 * you should only every have one output writer generating a given content type attached
	 * to the application.
	 * 
	 * @return the HTTP content type of the generated output.
	 */
	String getContentType();

	void output(Query query, OutputStream outputStream) throws IOException;
}
