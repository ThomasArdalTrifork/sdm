package com.trifork.sdm.replication.client;

import static java.lang.String.format;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.io.IOUtils;


public class Client {

	public static final int NO_LIMIT = -1;

	private final URL baseURL;


	public Client(String host, int port) throws MalformedURLException {

		this.baseURL = new URL("http", host, port, "");
	}


	public InputStream replicate(String resource) throws IOException {

		return replicate(resource, null, NO_LIMIT);
	}


	public InputStream replicate(String resource, int limit) throws IOException {

		return replicate(resource, null, limit);
	}


	public InputStream replicate(String resource, String offset) throws IOException {

		return replicate(resource, NO_LIMIT);
	}


	public InputStream replicate(String resource, String offset, int limit) throws IOException {

		String query = format("/gateway?resource=%s", resource);

		// TODO: Validate token.

		if (offset != null) {

			query += format("&token=%s", offset);
		}

		// Fetch the resource URL.

		HttpURLConnection connection = (HttpURLConnection) new URL(baseURL, query).openConnection();
		connection.connect();
		String resourceURL = IOUtils.toString(connection.getInputStream(), "UTF-8");

		// Fetch the resource.

		connection = (HttpURLConnection) new URL(resourceURL).openConnection();
		connection.connect();

		return connection.getInputStream();
	}
}
