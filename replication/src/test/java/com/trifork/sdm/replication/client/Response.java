package com.trifork.sdm.replication.client;

import java.io.InputStream;

public class Response {

	private int statusCode;
	private String contentType;
	private InputStream inputStream;

	Response(int statusCode, String contentType, InputStream inputStream) {
		this.statusCode = statusCode;
		this.contentType = contentType;
	}

	public int getStatusCode() {
		return statusCode;
	}

	public String getContentType() {
		return contentType;
	}
	
	/**
	 * Gets an input stream to the response body.
	 * 
	 * @return An open input stream to the content of the response body. 
	 */
	public InputStream getInputStream() {
		return inputStream;
	}
}
