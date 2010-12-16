package com.trifork.sdm.replication.client;

import static java.lang.String.format;
import static java.net.HttpURLConnection.HTTP_OK;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.inject.Inject;

import org.apache.commons.io.IOUtils;

import com.trifork.sdm.replication.configuration.properties.Host;
import com.trifork.sdm.replication.configuration.properties.Port;

public class Client {

	public static final int NO_LIMIT = 0;

	private final URL baseURL;

	@Inject
	Client(@Host String host, @Port int port) throws MalformedURLException {

		this.baseURL = new URL("http", host, port, "");
	}


	public InputStream replicate(String resource) throws IOException {
		
		return replicate(resource, NO_LIMIT);
	}


	public InputStream replicate(String resource, int limit) throws IOException {

		return replicate(resource, null, limit);
	}


	public InputStream replicate(String resource, String offset) throws IOException {

		return replicate(resource, offset, NO_LIMIT);
	}


	public InputStream replicate(String resource, String offset, int limit) throws IOException {
		
		String query = format("/gateway?resource=%s", resource);

		if (offset != null) {
			query += format("&token=%s", offset);
		}
		
		if (limit > NO_LIMIT) {
			query += format("&limit=%d", limit);
		}

		// Fetch the resource URL.

		HttpURLConnection connection = (HttpURLConnection) new URL(baseURL, query).openConnection();
		connection.connect();
		
		InputStream inputStream = null;
	
		if (connection.getResponseCode() == HTTP_OK) {
			
			String resourceURL = IOUtils.toString(connection.getInputStream(), "UTF-8");

			// Fetch the resource.
			connection = (HttpURLConnection) new URL(resourceURL).openConnection();
			connection.connect();
			
			inputStream = connection.getInputStream();
		}

		return inputStream;
	}
}
