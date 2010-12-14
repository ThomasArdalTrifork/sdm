package com.trifork.sdm.replication.service;

import static java.net.HttpURLConnection.HTTP_BAD_REQUEST;
import static java.net.HttpURLConnection.HTTP_FORBIDDEN;
import static java.net.HttpURLConnection.HTTP_GONE;

import java.io.IOException;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.trifork.sdm.replication.configuration.properties.Secret;
import com.trifork.sdm.replication.service.SignatureBuilder.HTTPMethod;


@Singleton
public class SecurityFilter implements Filter {

	private final String secret;

	private static final Set<String> specialParams = new HashSet<String>();
	{
		specialParams.add("signature");
		specialParams.add("expires");
		specialParams.add("username");
		specialParams.add("contenttype");
	}


	@Inject
	SecurityFilter(@Secret String secret) {

		this.secret = secret;
	}


	@Override
	public void init(FilterConfig filterConfig) throws ServletException {

	}


	@Override
	public void doFilter(ServletRequest rawRequest, ServletResponse rawResponse, FilterChain chain)
			throws IOException, ServletException {

		HttpServletRequest request = (HttpServletRequest) rawRequest;
		HttpServletResponse response = (HttpServletResponse) rawResponse;

		String bucket = request.getRequestURI().substring(1);

		// TODO: Validate the existence of these parameters.

		String signature = request.getParameter("signature");
		String username = request.getParameter("username");
		String expiresString = request.getParameter("expires");

		long expires;

		try {
			expires = Long.parseLong(expiresString);
		}
		catch (NumberFormatException e) {
			expires = Long.MIN_VALUE;
		}

		if (expires == Long.MIN_VALUE) {
			writeResponse(response, HTTP_BAD_REQUEST, "The request had an invalid 'expires'-parameter.");
		}
		else if (expires < System.currentTimeMillis()) {
			writeResponse(response, HTTP_GONE, "The requested resource has expired.");
		}
		else if (signature == null) {
			writeResponse(response, HTTP_FORBIDDEN, "The request did not contain a 'signature'-parameter.");
		}
		else if (username == null) {
			writeResponse(response, HTTP_FORBIDDEN, "The request did not contain a 'username'-parameter.");
		}
		else if (expiresString == null) {
			writeResponse(response, HTTP_FORBIDDEN, "The request did not contain a 'expires'-parameter.");
		}
		else if (verifySignature(request, bucket, signature, username, expires)) {
			chain.doFilter(request, response);
		}
		else {
			writeResponse(response, HTTP_FORBIDDEN, "The request's signature did not match the query.");
			// TODO: We might want to log this but it would make DOS attacks hurt more.
		}
	}


	private boolean verifySignature(HttpServletRequest request,
			String bucket, String signature, String username, long expires) throws IOException, ServletException {

		HTTPMethod method = HTTPMethod.valueOf(request.getMethod());

		SignatureBuilder builder = new SignatureBuilder(method, username, secret, bucket, expires);

		addQueryParameters(request, builder);

		String expectedSignature = builder.build();

		return expectedSignature.equals(signature);
	}


	private void addQueryParameters(HttpServletRequest request, final SignatureBuilder builder) {

		@SuppressWarnings("rawtypes")
		final Enumeration paramNames = request.getParameterNames();

		while (paramNames.hasMoreElements()) {

			final String parameter = (String) paramNames.nextElement();

			if (!specialParams.contains(parameter)) {

				final String value = request.getParameter(parameter);
				builder.addQueryParameter(parameter, value);
			}
		}
	}


	private void writeResponse(HttpServletResponse response, int statusCode, String message)
			throws IOException {

		response.setStatus(statusCode);
		final String output = String.format("%d %s", statusCode, message);
		response.getOutputStream().println(output);
	}


	@Override
	public void destroy() {

	}
}
