package com.trifork.sdm.replication;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Calendar;
import java.util.Map;
import java.util.TreeMap;

import javax.inject.Inject;

import org.apache.commons.io.IOUtils;
import org.hamcrest.Matcher;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;

import com.google.inject.Module;
import com.google.inject.Provides;
import com.google.inject.servlet.ServletModule;
import com.mysql.jdbc.AssertionFailedException;
import com.trifork.sdm.replication.configuration.properties.Host;
import com.trifork.sdm.replication.configuration.properties.Port;
import com.trifork.sdm.replication.junit.TestRunner;
import com.trifork.sdm.replication.service.Server;


@RunWith(TestRunner.class)
public abstract class ReplicationTest extends ServletModule {

	private static Server server;

	private static URL contextRoot;

	private static HttpURLConnection connection;

	private static Map<String, String> params = new TreeMap<String, String>();
	private static Map<String, String> headers = new TreeMap<String, String>();

	private static Calendar testTime;


	protected enum Day {
		NOW, YESTERDAY, TOMORROW, TWO_DAYS_AGO, THREE_DAYS_AGO, IN_TWO_DAYS, IN_THREE_DAYS
	}


	protected String date(Day day) {

		Calendar time = (Calendar) testTime.clone();

		switch (day) {
		case NOW:
			// Do nothing.
		case YESTERDAY:
			time.add(Calendar.DATE, -1);
		case TOMORROW:
			time.add(Calendar.DATE, 1);
		case TWO_DAYS_AGO:
			time.add(Calendar.DATE, -2);
		case THREE_DAYS_AGO:
			time.add(Calendar.DATE, -3);
		case IN_TWO_DAYS:
			time.add(Calendar.DATE, 2);
		case IN_THREE_DAYS:
			time.add(Calendar.DATE, 3);
		}

		return Long.toString(time.getTimeInMillis() / 1000);
	}
	
	public Module[] getConfiguration() {
		
		return new Module[] {};
	}


	@Override
	public final void configureServlets() {

		initialize();
	}


	public abstract void initialize();


	@Inject
	@Provides
	public URL provideURL(@Host String host, @Port int port) throws IOException {

		return new URL("http", host, port, "/");
	}


	@Inject
	@Provides
	public HttpURLConnection provideConnection(URL server) throws IOException {

		HttpURLConnection connection = (HttpURLConnection) server.openConnection();

		connection.setUseCaches(false);
		connection.setDoOutput(true);

		return connection;
	}


	@BeforeClass
	public static void start(Server server) throws MalformedURLException {

		testTime = Calendar.getInstance();

		ReplicationTest.server = server;
		new Thread(server).run();

		ReplicationTest.contextRoot = new URL("http", "0.0.0.0", 3001, "");
	}


	@AfterClass
	public static void finish() {

		server.stop();
	}


	protected String readInputStream(URLConnection connection) throws IOException {

		return IOUtils.toString(connection.getInputStream(), "UTF-8");
	}


	protected void get(String resource) {

		try {
			StringBuilder path = new StringBuilder(resource);

			boolean first = true;

			for (String key : params.keySet()) {

				if (first)
					path.append('?');
				else
					path.append('&');

				path.append(key);
				path.append('=');
				path.append(params.get(key));

				first = false;
			}

			connection = (HttpURLConnection) new URL(contextRoot, path.toString()).openConnection();

			for (String key : headers.keySet()) {

				connection.addRequestProperty(key, headers.get(key));
			}

			connection.connect();
		}
		catch (Exception e) {

			throw new AssertionFailedException(e);
		}
	}


	protected void setParam(String key, String value) {

		params.put(key, value);
	}


	protected void setHeader(String key, String value) {

		headers.put(key, value);
	}


	protected void printContent() throws IOException {

		System.out.println(readInputStream(connection));
	}


	// Assertions

	protected void assertStatus(int status) {

		try {
			assertThat(connection.getResponseCode(), is(status));
		}
		catch (Exception e) {
			throw new AssertionFailedException(e);
		}
	}


	protected void assertXML(String xpath, Matcher<Object> valueMatcher) {

	}
}
