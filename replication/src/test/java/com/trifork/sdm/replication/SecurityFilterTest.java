package com.trifork.sdm.replication;

import static com.trifork.sdm.replication.TestHelper.HTTP_UNAUTHORIZED;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.EventListener;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mortbay.resource.Resource;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.ServletModule;
import com.sun.corba.se.impl.orb.ParserTable.TestContactInfoListFactory;
import com.trifork.sdm.replication.client.Client;
import com.trifork.sdm.replication.client.Response;
import com.trifork.sdm.replication.configuration.ConfigurationModule;
import com.trifork.sdm.replication.configuration.ResourceDispatcher;
import com.trifork.sdm.replication.configuration.ServerModule;
import com.trifork.sdm.replication.security.SecurityFilter;

public class SecurityFilterTest {

	private static final String host = "0.0.0.0";
	private static final int port = 3001;

	private static URL endpoint;
	private static Server server;

	private static final String RESPONSE_DATA = "TEST DATA";

	@BeforeClass
	public static void initialize() throws Exception {

		try {
			Injector injector = Guice.createInjector(new TestConfigurationModule(), new AbstractModule() {

				@Override
				protected void configure() {
					
					bind(EventListener.class).to(FilterTestResourceDispatcher.class);
				}
			});

			server = injector.getInstance(Server.class);
			new Thread(server).start();

			endpoint = new URL("http://0.0.0.0:3001");
		}
		catch (Throwable t) {
			t.printStackTrace();
			throw new Exception(t);
		}
	}

	@AfterClass
	public static void destroy() {

		server.stop();
	}

	@Test
	public void requests_with_no_authorization_header_should_be_rejected() throws IOException {

		Client client = new Client(endpoint, "gateway", "test");

		Response response = client.get("/apotek");

		assertThat(response.getStatusCode(), is(HTTP_UNAUTHORIZED));
	}

	public void request_with_invalid_user_signature_combimnation_should_be_rejected() throws IOException {

		Client client = new Client(endpoint, "gateway", "test");

		Response response = client.get("/apotek");

		assertThat(response.getStatusCode(), is(HTTP_UNAUTHORIZED));
	}
}
