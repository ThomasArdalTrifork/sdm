package com.trifork.sdm.replication.security;

import java.io.IOException;

import javax.inject.Singleton;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;

@Singleton
public class HeaderSecurityFilter implements Filter {

	private static int HTTP_BAD_REQUEST = 400;
	private static int HTTP_UNAUTHORIZED = 401;

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {

	}

	@Override
	public void doFilter(ServletRequest rawRequest, ServletResponse rawResponse, FilterChain chain)
			throws IOException, ServletException {

		HttpServletRequest request = (HttpServletRequest) rawRequest;
		HttpServletResponse response = (HttpServletResponse) rawResponse;

		if (validateAuthorization(request, response)) {
			
			chain.doFilter(request, response);
		}
	}

	/**
	 * The connection is secure we want to extract the Authorization
	 * 
	 * information from the headers.
	 * 
	 * We expect a Authorization header with the format:
	 * 
	 * <code>Authorization: SDM [public user id]:[signed headers]</code>
	 * 
	 * Where the string in front of the colon is the user name, and the string
	 * after it is the message headers signed with the user's key.
	 * 
	 * See {@link Token} for more information about the authorization protocol.
	 * 
	 * @throws IOException
	 */
	private boolean validateAuthorization(HttpServletRequest request, HttpServletResponse response)
			throws IOException {

		final String authorization = request.getHeader("Authorization");
		
		if (authorization == null) {
			writeResponse(response, HTTP_UNAUTHORIZED, "The 'Authorization' header is missing the the HTTP request.");
			return false;
		}

		try {
			// TODO: Clean the splitting up.
			final String protocol = authorization.split(" ")[0];
			final String credentials = authorization.split(" ")[1];
			final String userID = credentials.split(":")[0];
			final String signature = credentials.split(":")[1];

			if (!protocol.equals("SDM")) {

			}
			else if (!userID.equals("gateway")) {

				// Right now we only support one user, the gateway.
				// In future we might support additional users so it should be
				// included.

			}
			else {
				
				// Missing headers are left out but their '\n' separators are still required.

				final StringBuilder builder = new StringBuilder();
				
				final String method = request.getMethod();
				
				if (!"GET".equals(method)) {
					// We only support the GET verb and might
					// also only support this in the future.
					writeResponse(response, HTTP_BAD_REQUEST, "The resource path is missing from the request.");
					return false;
				}
				
				builder.append(method);
				builder.append('\n');
				
				// NOTE: A place-holder for future compatibility with content checking,
				// e.g. for using the PUT verb.
				
				final String contentMD5 = request.getHeader("Content-Md5");
				
				if (contentMD5 != null) builder.append(contentMD5);
				builder.append('\n');
				
				// Expected content type is also required.
				
				final String contentType = request.getContentType();
				
				// PUT and POST require a content type. 
				
				if ((method.equals("PUT") || method.equals("POST")) && contentType == null) {
					writeResponse(response, HTTP_BAD_REQUEST, "The Content-Type header is missing from the request.");
					return false;
				}
				
				if (contentMD5 != null) builder.append(contentType);
				builder.append('\n');
				
				// Since the client might not be able to set the date
				// header in the framework they use, we require a custom header
				// for the same purpose.

				final String date = request.getHeader("X-Sdm-Date");

				if (date == null) {
					writeResponse(response, HTTP_BAD_REQUEST, "The Date header is missing from the request.");
					return false;
				}
				
				builder.append("x-sdm-date:" + date);
				builder.append('\n');
				
				final String resource = request.getRequestURI();
				
				if (resource == null) {
					writeResponse(response, HTTP_BAD_REQUEST, "The resource path is missing from the request.");
					return false;
				}
				
				builder.append(resource);
				
				// Using the service's public key we verify that the request was
				// authorized by the server. In future we might have to authenticate
				// individual clients instead.
				
				String expectedSignature = builder.toString().toLowerCase();
				expectedSignature = new String(Base64.decodeBase64(signature));
				
				if (!signature.equals(expectedSignature)) {
					writeResponse(response, HTTP_UNAUTHORIZED, "The message signature in the 'Authorization' header was incorrect.\nThe event has been logged.");
					// TODO: Notify or Log.
					return false;
				}
			}

		}
		catch (ArrayIndexOutOfBoundsException e) {
			writeResponse(response, HTTP_UNAUTHORIZED, "The authentication header was not formatted correctly.");
			return false;
		}

		// The user was authorized to access the requested resource.
		
		return true;
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
