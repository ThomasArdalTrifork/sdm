package com.trifork.sdm.replication;

import java.io.IOException;
import java.io.OutputStream;


/**
 * Writes entity instances to an output stream.
 * 
 * Implementing classes should support a single output format e.g. XML or
 * FastInfoset.
 */
public interface EntityWriter {

	/**
	 * Implementing classes must ensure (read assert) that the instance given if
	 * of the entity type it supports.
	 * 
	 * @param instance
	 *            the instance entity the entity you want to output.
	 */
	void write(Object entityInstance, OutputStream outputStream) throws IOException;
	
	/**
	 * Implementing classes must return the HTTP content type for the generated output,
	 * you should only every have one output writer generating a given content type attached
	 * to the application.
	 * 
	 * @return the HTTP content type of the generated output.
	 */
	String getContentType();
}
