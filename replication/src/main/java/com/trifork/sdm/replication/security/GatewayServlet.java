package com.trifork.sdm.replication.security;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.util.Calendar;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.trifork.sdm.replication.configuration.properties.AuthorizationTTL;
import com.trifork.sdm.replication.configuration.properties.Host;
import com.trifork.sdm.replication.configuration.properties.PageSize;
import com.trifork.sdm.replication.configuration.properties.Port;
import com.trifork.sdm.replication.configuration.properties.Secret;


@Singleton
public class GatewayServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private final int timeToLive;

	private final String username = "gateway";
	private final String key;

	private final DateFormat dateFormat;
	private final URL baseURL;


	@Inject
	GatewayServlet(@Secret String key, @AuthorizationTTL int ttl, @PageSize int defaultPageSize,
			DateFormat dateFormat, @Host String host, @Port int port) throws MalformedURLException {

		
		assert key != null && !key.isEmpty();
		assert ttl > 0;
		assert defaultPageSize > 0;
		assert dateFormat != null;
		assert host != null;

		this.key = key;
		this.timeToLive = ttl;
		this.dateFormat = dateFormat;
		this.baseURL = new URL("http", host, port, "");
	}


	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException,
			IOException {

		// FIXME: Pull the request's data from the SOAP envelope, and
		// authenticate.

		final String bucket = request.getHeader("X-Sdm-Bucket");

		if (bucket == null || bucket.isEmpty()) {

			response.setStatus(HttpURLConnection.HTTP_BAD_REQUEST);
			response.getOutputStream().println("A request must contain a 'X-Sdm-Bucket'-header.");
		}
		else {

			// Authorize the user for a time window.
			Calendar expires = Calendar.getInstance();
			expires.add(Calendar.SECOND, timeToLive);

			// HACK: Don't hard-code the host and port.
			final URL bucketURL = new URL(baseURL, "/" + bucket);

			URLBuilder builder = new URLBuilder(bucketURL, username, key, expires.getTime());

			// Query parameters

			// NOTE: At the moment we only support one query parameter,
			// namely since. This should be generalized in the future.

			final String sinceHeader = request.getHeader("X-Sdm-Since");

			// Since is not required. If it isn't there we make an
			// initialization.
			if (sinceHeader != null) {
				// We parse the header to make sure it is a value date.
				// TODO: We might make this a bit more advanced.
				final long since = Long.parseLong(sinceHeader);
				builder.setQueryParameter("since", Long.toString(since));
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
