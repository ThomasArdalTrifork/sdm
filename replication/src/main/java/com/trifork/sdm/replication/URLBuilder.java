package com.trifork.sdm.replication;

import java.net.URL;
import java.util.Date;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import com.trifork.sdm.replication.SignatureBuilder.HTTPMethod;


public class URLBuilder {

	private final String username;
	private final Date expires;

	private Map<String, String> params;
	private final URL bucketURL;
	private final String secret;


	public URLBuilder(URL bucketURL, String username, String secret, Date expires) {

		/*
		assert username != null;
		assert expires != null;
		assert bucketURL != null;
		assert secret != null;
		*/

		this.username = username;
		this.expires = expires;
		this.bucketURL = bucketURL;
		this.secret = secret;
	}


	public URLBuilder setQueryParameter(String key, String value) {

		if (params == null) {
			params = new TreeMap<String, String>();
		}

		params.put(key, value);

		return this;
	}


	public String build() {

		StringBuilder builder = new StringBuilder();

		builder.append(bucketURL);
		builder.append('?');

		String bucket = bucketURL.getFile().substring(1);

		SignatureBuilder signature = new SignatureBuilder(HTTPMethod.GET, username, secret, bucket,
				expires.getTime());

		if (params != null)
		for (Entry<String, String> param : params.entrySet()) {

			signature.addQueryParameter(param.getKey(), param.getValue());

			builder.append(param.getKey());
			builder.append('=');
			builder.append(param.getValue());
			
			builder.append('&');
		}
		
		builder.append("username=");
		builder.append(username);

		builder.append("&expires=");
		builder.append(expires.getTime());
		
		builder.append("&signature=");

		try {
			builder.append(signature.build());
		}
		catch (Throwable t) {
			throw new RuntimeException(
					"Error while generating the resource URL. This is an error in the API.", t);
		}

		return builder.toString();
	}
}
