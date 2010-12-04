package com.trifork.sdm.replication.client;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;

/**
 * A SOAP Client that communicates with the SDM server.
 */
public class SoapClient {
	
	public static enum Protocol {
		HTTP,
		HTTPS
	}
	
	private Protocol protocol;
	
	/**
	 * Creates a new SOAP client.
	 * 
	 * @param serverURL The URL to the SDM server's root.
	 */
	public SoapClient(URL serverURL) {
		
		// All communication should be done with.
		
		this.protocol = Protocol.HTTPS;
	}
	
	public Response getUpdates(String resource, Date since) throws IOException {		
		
		final URL resourceURL = new URL(resource);
		
		HttpURLConnection connection = (HttpURLConnection)resourceURL.openConnection();
		
		// FIXME: Christian or Jan should set the SOAP request here.
		connection.setRequestProperty("X-Resource", resource);
		connection.setRequestProperty("X-Since", since.toString());
		connection.setUseCaches(false);
		
		connection.connect();
		
		Response response = new Response(
				connection.getResponseCode(),
				connection.getContentType(),
				connection.getInputStream());
		
		return response;
	}
	
	/**
	 * Sets the HTTP protocol variant to use.
	 * 
	 * You should always set this to HTTPS except for testing purposes,
	 * or other special circumstances.
	 */
	public void setProtocol(Protocol protocol) {
		
		this.protocol = protocol;
	}
	
	/**
	 * @returns The protocol used to communicate with the SDM server.
	 */
	public Protocol getProtocol() {
		
		return protocol;
	}
}
