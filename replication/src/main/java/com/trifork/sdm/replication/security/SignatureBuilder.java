package com.trifork.sdm.replication.security;

import java.security.SignatureException;

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
	private String resource;
	private String key;
	// private String contentType;
	private long expires ;

	private static final String HMAC_SHA1_ALGORITHM = "HmacSHA1";

	public SignatureBuilder(String key, String resource, long expires) {

		this.resource = resource;
		this.key = key;
		this.expires = expires;

		method = HTTPMethod.GET;
	}

	public SignatureBuilder setMethod(HTTPMethod method) {

		this.method = method;
		return this;
	}

	public SignatureBuilder setSecret(String secret) {

		this.key = secret;
		return this;
	}

	public SignatureBuilder setExpires(long expires) {

		this.expires = expires;
		return this;
	}

	/*
	public SignatureBuilder setContentType(String contentType) {

		this.contentType = contentType;
		return this;
	}
	*/

	public String build() throws IllegalStateException, SignatureException {

		// Validate the signature.

		if (method == null) {
			throw new IllegalStateException("A signature must have a HTTP method field.");
		}

		// TODO: A GET request should not include a content type.

		if (resource == null) {
			throw new IllegalStateException("A signature must have a resource field.");
		}

		String messageDescription = this.toString();

		return calculateRFC2104HMAC(messageDescription, key);
	}

	@Override
	public String toString() {

		final StringBuilder signature = new StringBuilder();

		signature.append(method);
		signature.append('\n');

		// NOTE: A place-holder for future compatibility with content checking,
		// e.g. for using the PUT verb.

		// final String contentMD5 = request.getHeader("Content-Md5");

		// if (contentMD5 != null) builder.append(contentMD5);
		//signature.append('\n'); // We leave this in for forward compatibility.

		// Expected content type is also required.

		// if (contentType != null) builder.append(contentType);
		signature.append('\n'); // We leave this in for forward compatibility.

		// Since the client might not be able to set the date
		// header in the framework they use, we require a custom header
		// for the same purpose.

		signature.append(expires);
		signature.append('\n');

		// Lastly we append the resource path.

		signature.append(resource);

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
	private static String calculateRFC2104HMAC(String data, String key)
			throws SignatureException {

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
			throw new SignatureException("Failed to generate HMAC : " + e.getMessage());
		}

		return result;
	}
}
