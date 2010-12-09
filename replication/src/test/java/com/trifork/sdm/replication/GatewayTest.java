package com.trifork.sdm.replication;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.number.OrderingComparison.*;
import static org.junit.Assert.*;

import java.io.IOException;
import java.net.HttpURLConnection;
import static java.net.HttpURLConnection.*;
import java.net.URL;
import java.security.SignatureException;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;

import com.trifork.sdm.replication.configuration.Bucket;
import com.trifork.sdm.replication.configuration.properties.Secret;
import com.trifork.sdm.replication.security.GatewayServlet;
import com.trifork.sdm.replication.security.SignatureBuilder;
import com.trifork.sdm.replication.security.SignatureBuilder.HTTPMethod;


public class GatewayTest extends ReplicationTest {

	private static final String SINCE_PARAMETER = "since=(\\d+)";
	private static final String USERNAME_PARAMETER = "username=([^&]+)";
	private static final String SIGNATURE_PARAMETER = "signature=([^&]+)";
	private static final String EXPIRES_PARAMETER = "expires=(\\d+)";

	private final String secret = "secret";
	private final String bucket = "apotek";

	@SuppressWarnings("deprecation")
	private static final Date PAST_DATE = new Date(2000 - 1900, 1, 1);
	private static final String GATEWAY_USERNAME = "gateway";


	@Override
	public void initialize(ConnectionManager manager) {

		bindConstant().annotatedWith(Bucket.class).to("/gateway");
		serve("/gateway").with(GatewayServlet.class);

		bindConstant().annotatedWith(Secret.class).to(secret);
	}
	
	
	@Test
	public void should_require_a_bucket_header(HttpURLConnection connection) throws IOException {

		connection.connect();

		// TODO: The expected body should be a SOAP envelope.

		assertThat(connection.getResponseCode(), is(HTTP_BAD_REQUEST));
	}


	@Test
	public void should_return_a_valid_SOAP_envelope(HttpURLConnection connection) throws IOException {

		connection.setRequestProperty("X-Sdm-Bucket", bucket);
		connection.connect();

		// TODO: The expected body should be a SOAP envelope.

		String response = readInputStream(connection);
		new URL(response);
	}


	@Test
	public void should_return_an_expires_date_in_the_future(HttpURLConnection connection) throws IOException {

		connection.setRequestProperty("X-Sdm-Bucket", bucket);
		connection.connect();
		String response = readInputStream(connection);

		final String parameter = getURLSegment(response, EXPIRES_PARAMETER);
		long expires = Long.parseLong(parameter);
		long currentTime = System.currentTimeMillis();
		assertThat(expires, is(greaterThan(currentTime)));
	}


	@Test
	public void should_return_a_signature_that_matches_a_request_with_no_parameters(
			HttpURLConnection connection) throws IOException, IllegalStateException, SignatureException {

		connection.setRequestProperty("X-Sdm-Bucket", bucket);
		connection.connect();

		String response = readInputStream(connection);

		String parameter = getURLSegment(response, EXPIRES_PARAMETER);
		long expires = Long.parseLong(parameter);

		SignatureBuilder signatureBuilder = new SignatureBuilder(HTTPMethod.GET, GATEWAY_USERNAME, secret,
				bucket, expires);
		String expectedSignature = signatureBuilder.build();

		String signature = getURLSegment(response, SIGNATURE_PARAMETER);

		assertThat(signature, is(equalTo(expectedSignature)));
	}


	@Test
	public void should_return_a_username(HttpURLConnection connection) throws IOException,
			IllegalStateException, SignatureException {

		connection.setRequestProperty("X-Sdm-Bucket", bucket);
		connection.connect();

		String response = readInputStream(connection);

		String parameter = getURLSegment(response, USERNAME_PARAMETER);

		assertNotNull(parameter);
	}


	@Test
	public void requests_must_contain_a_bucket(HttpURLConnection connection) throws IOException {

		connection.connect();
		assertThat(connection.getResponseCode(), is(HttpURLConnection.HTTP_BAD_REQUEST));
	}


	@Test
	public void should_return_a_signature_and_query_that_matches_a_request_with_parameters(
			HttpURLConnection connection) throws IOException, IllegalStateException, SignatureException {

		connection.setRequestProperty("X-Sdm-Bucket", bucket);
		connection.setRequestProperty("X-Sdm-Since", Long.toString(PAST_DATE.getTime()));
		connection.connect();

		String response = readInputStream(connection);

		String expiresParameter = getURLSegment(response, EXPIRES_PARAMETER);
		long expires = Long.parseLong(expiresParameter);

		SignatureBuilder signatureBuilder = new SignatureBuilder(HTTPMethod.GET, GATEWAY_USERNAME, secret,
				bucket, expires).setSince(PAST_DATE);

		Date sinceParameter = new Date(Long.parseLong(getURLSegment(response, SINCE_PARAMETER)));
		assertThat(sinceParameter, is(equalTo(PAST_DATE)));

		String expectedSignature = signatureBuilder.build();
		String signature = getURLSegment(response, SIGNATURE_PARAMETER);
		assertThat(signature, is(equalTo(expectedSignature)));
	}


	// --------------------------------------------------------------------------------------------
	// Test Helper Methods
	// --------------------------------------------------------------------------------------------

	protected String getURLSegment(String response, String regexp) throws IOException {

		URL resourceURL = new URL(response);

		Matcher matcher = Pattern.compile(".*" + regexp + ".*").matcher(resourceURL.getQuery());

		String value = null;

		if (matcher.find()) {
			value = matcher.group(1);
		}

		return value;
	}
}
