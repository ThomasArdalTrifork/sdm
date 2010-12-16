package com.trifork.sdm.replication;

import static java.net.HttpURLConnection.HTTP_BAD_REQUEST;
import static java.net.HttpURLConnection.HTTP_FORBIDDEN;
import static java.net.HttpURLConnection.HTTP_GONE;
import static java.net.HttpURLConnection.HTTP_OK;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Test;

import com.google.inject.Module;
import com.trifork.sdm.replication.configuration.Bucket;
import com.trifork.sdm.replication.configuration.properties.Secret;
import com.trifork.sdm.replication.service.SecurityFilter;
import com.trifork.sdm.replication.service.URLBuilder;


public class SecurityFilterTest extends ReplicationTest {

	private String secret = "ef_fwefoihe%wu32ew";
	private String username = "gateway";
	private final static String bucket = "/resource";


	@Override
	public Module[] getConfiguration() {

		return new Module[] { this };
	}


	@Override
	public void initialize() {

		// The filter is what we are under testing.

		filter("/*").through(SecurityFilter.class);

		bindConstant().annotatedWith(Bucket.class).to(bucket);

		// Serve everything through a mock servlet.

		serve("/resource").with(new HttpServlet() {

			private static final long serialVersionUID = 1L;


			@Override
			protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

				resp.setStatus(HTTP_OK);
			}
		});

		bindConstant().annotatedWith(Secret.class).to(secret);
	}


	@Test
	public void should_allowed_correct_requests(URL bucketURL) {

		final String correctURL = new URLBuilder(bucketURL, username, secret, getTomorrow()).build();
		assertStatus(correctURL, HTTP_OK);
	}


	@Test
	public void should_have_a_valid_expires_date(URL bucketURL) {

		Date expires = getTomorrow();
		String tomorrow = Long.toString(expires.getTime());
		String invalidDateFormat = "78S66(2i8";

		String correctURL = new URLBuilder(bucketURL, username, secret, expires).build();

		assertURLNotAuthorized(correctURL, tomorrow, invalidDateFormat, HTTP_BAD_REQUEST);
	}


	@Test
	public void should_rejects_stale_requests(URL bucketURL) {

		final String staleURL = new URLBuilder(bucketURL, username, secret, getYesterday()).build();
		assertStatus(staleURL, HTTP_GONE);
	}


	@Test
	public void request_with_a_signature_that_does_not_match_should_be_rejected(URL bucketURL) throws IOException {

		Date expires = getTomorrow();

		String yesterday = Long.toString(getYesterday().getTime());
		String twoDaysAgo = Long.toString(getTwoDaysAgo().getTime());
		String tomorrow = Long.toString(expires.getTime());
		String inTwoDays = Long.toString(getInTwoDays().getTime());

		String correctURL = new URLBuilder(bucketURL, username, secret, expires).setQueryParameter("since", yesterday).build();

		assertURLNotAuthorized(correctURL, bucket, "/otherResource", HTTP_FORBIDDEN);

		assertURLNotAuthorized(correctURL, username, "otherUser", HTTP_FORBIDDEN);

		assertURLNotAuthorized(correctURL, yesterday, twoDaysAgo, HTTP_FORBIDDEN);

		assertURLNotAuthorized(correctURL, tomorrow, inTwoDays, HTTP_FORBIDDEN);
	}


	protected void assertURLNotAuthorized(String correctURL, String replacedString, String replacement, int expectedStatus) {

		String invalidURL = correctURL.replace(replacedString, replacement);
		assertStatus(invalidURL, expectedStatus);
	}


	protected void assertStatus(String urlString, int expectedStatus) {

		try {
			URL url = new URL(urlString);

			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.connect();

			assertThat(connection.getResponseCode(), is(expectedStatus));
		}
		catch (Exception e) {
			fail();
		}
	}


	protected Date getInTwoDays() {

		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DATE, 2);
		return calendar.getTime();
	}


	protected Date getTomorrow() {

		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DATE, 1);
		return calendar.getTime();
	}


	protected Date getYesterday() {

		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DATE, -1);
		return calendar.getTime();
	}


	protected Date getTwoDaysAgo() {

		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DATE, -2);
		return calendar.getTime();
	}
}
