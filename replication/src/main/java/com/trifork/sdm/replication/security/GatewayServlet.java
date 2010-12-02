package com.trifork.sdm.replication.security;

import java.io.IOException;
import java.security.SignatureException;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.omg.CORBA.Current;

import com.trifork.sdm.replication.configuration.properties.AuthorizationTTL;
import com.trifork.sdm.replication.configuration.properties.Key;
import com.trifork.sdm.replication.configuration.properties.PageSize;

@Singleton
public class GatewayServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private static final int UNDEFINED = -1;

	private final int timeToLive;
	private final int defaultPageSize;

	private final String key;

	@Inject
	GatewayServlet(@Key String key, @AuthorizationTTL int ttl, @PageSize int defaultPageSize) {

		assert key != null && !key.isEmpty();
		assert ttl > 0;
		assert defaultPageSize > 0;

		this.key = key;
		this.timeToLive = ttl;
		this.defaultPageSize = defaultPageSize;
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException,
			IOException {

		// TODO: Authenticate the user.

		// TODO: Pull the request's data from the SOAP envelope.

		final Calendar since = Calendar.getInstance();
		since.add(Calendar.MONTH, -6);

		int pageSize = UNDEFINED; // TODO: Read pageSize from request.

		if (pageSize == UNDEFINED) pageSize = defaultPageSize;

		// Define the time window where the resource will be accessible.

		// Authorize the user for a time window.
		
		long expires = System.currentTimeMillis() / 1000 + timeToLive;
		
		final String resource = "/apotek"; // TODO: Read the requested entity from the request.

		SignatureBuilder signatureBuilder = new SignatureBuilder(key, resource, expires);
		
		String signature = null;
		
		try {
			signature = signatureBuilder.build();
			
			// Create the response.

			// TODO: Use response.setContentType("application/soap+xml"); instead
			response.setContentType("text");
			response.setStatus(200);

			// HACK: Don't hard-code the host and port.
			final String templateURL = "http://0.0.0.0:3001%s?signature=%s&expires=%s";
			String resourceURL = String.format(templateURL, resource, signature, expires);
			
			// TODO: Should be written in a DGWS envelope.
			response.getOutputStream().println(resourceURL);
		}
		catch (SignatureException e) {
			
			// TODO: Notify the operator. This should not happen, and is a programming error.
		}
	}
}
