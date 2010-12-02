package com.trifork.sdm.replication.client;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;

import javax.net.ssl.HttpsURLConnection;

public class Client {
	
	private final URL server;
	
	public Client(URL server, String userID, String privateKey) {
		
		this.server = server;
	}
	
	public Response get(String resource) throws IOException {
		
		final URL resourceURL = new URL(server, resource);
		
		HttpURLConnection connection = (HttpURLConnection)resourceURL.openConnection();
	
		// SDM Expects all time data is in UTC time.
		final Date timeUTC = new Date();
		
		connection.setRequestProperty("X-Sdm-Date", timeUTC.toString());
		connection.setRequestProperty("Authorization", "SDM gateway:test");

		connection.setUseCaches(false);
		connection.setDoOutput(true);
		
		connection.connect();
		
		Response response = new Response(connection.getResponseCode(), connection.getResponseMessage());
		
		return response;
	}
}
