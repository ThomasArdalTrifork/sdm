package com.trifork.sdm.replication.client;

public class Response {

	private int statusCode = -1;
	private String contentType = null;

	Response(int statusCode, String contentType) {
		this.statusCode = statusCode;
		this.contentType = contentType;
	}

	public int getStatusCode() {
		return statusCode;
	}

	public String getContentType() {
		return contentType;
	}
}
