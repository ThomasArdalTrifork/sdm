package com.trifork.sdm.replication.service;

import java.security.SignatureException;
import java.util.Date;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;


public class SignatureBuilder {

	public static enum HTTPMethod {
		GET("GET"), PUT("PUT"), HEAD("HEAD"), DELETE("DELETE"), POST("POST");

		private final String name;


		private HTTPMethod(String name) {

			this.name = name;
		}


		@Override
		public String toString() {

			return name;
		}
	}


	private HTTPMethod method;
	private String bucket;
	private String password;
	private long expires;
	private Map<String,String> queryParameters;
	
	
	private static final String HMAC_SHA1_ALGORITHM = "HmacSHA1";
	private final String username;
	
	
	public SignatureBuilder(HTTPMethod method, String username, String password, String bucket, long expires) {
		
		/*
		assert method != null;
		assert username != null;
		assert password != null;
		assert bucket != null;
		*/

		this.method = method;
		this.username = username;
		this.bucket = bucket;
		this.password = password;
		this.expires = expires;
	}
	

	public String build() {

		String signature = null;
		
		try {
			signature = calculateRFC2104HMAC(toString(), password);
		}
		catch (Exception e) {
			// This should never happen, and is a programming error.
			// TODO: Notify.
		}

		return signature;
	}


	@Override
	public String toString() {

		final StringBuilder signature = new StringBuilder();
		
		// TODO: Look into header unfolding and trimming which might cause trouble.

		signature.append(method);
		signature.append('\n');

		// NOTE: A place-holder for future compatibility with content checking,
		// e.g. for using the PUT verb.

		// final String contentMD5 = request.getHeader("Content-Md5");

		// if (contentMD5 != null) builder.append(contentMD5);
		signature.append('\n'); // We leave this in for forward compatibility.

		// Expected content type is also required.

		// if (contentType != null) builder.append(contentType);
		signature.append('\n'); // We leave this in for forward compatibility.
		
		signature.append(username);
		signature.append('\n');

		// Since the client might not be able to set the date
		// header in the framework they use, we require a custom header
		// for the same purpose.

		signature.append(expires);
		signature.append('\n');

		// Lastly we append the canonical resource.
		signature.append("/" + bucket);
		
		// and the query parameters attached.
		if (queryParameters != null)
		for (Entry<String, String> queryEntry : queryParameters.entrySet()) {
			signature.append('\n');
			signature.append(queryEntry.getKey());
			signature.append(':');
			signature.append(queryEntry.getValue());
		}
		
		return signature.toString().toLowerCase();
	}


	/**
	 * Computes RFC 2104-complaint HMAC signature.
	 * 
	 * @param data
	 *            The data to be signed.
	 * @param key
	 *            The signing key.
	 * @return The Base64-encoded RFC 2104-complaint HMAC signature.
	 * @throws java.security.SignatureException
	 *             when signature generation fails
	 */
	private static String calculateRFC2104HMAC(String data, String key) throws SignatureException {

		String result;

		try {
			// get an hmac_sha1 key from the raw key bytes
			SecretKeySpec signingKey = new SecretKeySpec(key.getBytes(), HMAC_SHA1_ALGORITHM);

			// get an hmac_sha1 Mac instance and initialize with the signing key
			Mac mac = Mac.getInstance(HMAC_SHA1_ALGORITHM);
			mac.init(signingKey);

			// compute the hmac on input data bytes
			byte[] rawHmac = mac.doFinal(data.getBytes());

			// base64-encode the hmac
			result = Base64.encodeBase64URLSafeString(rawHmac);
		}
		catch (Exception e) {
			throw new SignatureException("Failed to generate HMAC encoding: " + e.getMessage());
		}

		return result;
	}


	public SignatureBuilder setSince(Date since) {

		addQueryParameter("since", Long.toString(since.getTime()));
		return this;
	}
	
	public void addQueryParameter(String key, String value) {
		
		if (queryParameters == null) {
			queryParameters = new TreeMap<String, String>();
		}
		
		queryParameters.put(key, value);
	}
}
