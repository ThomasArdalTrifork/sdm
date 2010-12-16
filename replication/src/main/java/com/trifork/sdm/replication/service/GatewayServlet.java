package com.trifork.sdm.replication.service;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.trifork.sdm.replication.configuration.properties.AuthorizationTTL;
import com.trifork.sdm.replication.configuration.properties.DefaultPageSize;
import com.trifork.sdm.replication.configuration.properties.Host;
import com.trifork.sdm.replication.configuration.properties.Port;
import com.trifork.sdm.replication.configuration.properties.Secret;


@Singleton
public class GatewayServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private final int timeToLive;

	private final String username = "gateway";
	private final String key;

	private final URL baseURL;

	@Inject
	Map entities;


	@Inject
	GatewayServlet(@Secret String key, @AuthorizationTTL int ttl, @DefaultPageSize int defaultPageSize,
			@Host String host, @Port int port) throws MalformedURLException {

		/*
		 * TODO: Use preconditions from JSR-305 instead assert key != null &&
		 * !key.isEmpty(); assert ttl > 0; assert defaultPageSize > 0; assert
		 * dateFormat != null; assert host != null;
		 */

		this.key = key;
		this.timeToLive = ttl;
		this.baseURL = new URL("http", host, port, "");
	}


	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException,
			IOException {

		// FIXME: Pull the request's data from the SOAP envelope, and
		// authenticate.

		final String resource = request.getParameter("resource");

		if (resource == null || resource.isEmpty()) {

			response.setStatus(HttpURLConnection.HTTP_BAD_REQUEST);
			response.getOutputStream().println("A request must contain a 'resource' parameter.");
		}
		else {

			// Authorize the user for a time window.
			Calendar expires = Calendar.getInstance();
			expires.add(Calendar.SECOND, timeToLive);

			// HACK: Don't hard-code the host and port.
			URL bucketURL = new URL(baseURL, "/" + resource);

			URLBuilder builder = new URLBuilder(bucketURL, username, key, expires.getTime());

			// Query parameters

			// NOTE: At the moment we only support one query parameter,
			// namely since. This should be generalized in the future.

			// Since is not required. If it isn't there we make an
			// initialization.

			String token = request.getParameter("token");
			
			if (token != null) {
				
				builder.setQueryParameter("token", token);
			}

			// Create the response.
			response.setContentType("application/soap+xml; charset=UTF-8");
			response.setStatus(200);

			String resourceURL = builder.build();

			// TODO: Should be written in a DGWS envelope.
			response.getOutputStream().println(resourceURL);
		}

		// TODO: Should we even do this?
		response.getOutputStream().flush();
		response.getOutputStream().close();
	}
}
