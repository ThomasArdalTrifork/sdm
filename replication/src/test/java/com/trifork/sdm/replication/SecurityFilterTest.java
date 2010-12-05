package com.trifork.sdm.replication;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Test;

import com.trifork.sdm.replication.configuration.Resource;
import com.trifork.sdm.replication.configuration.properties.Secret;
import com.trifork.sdm.replication.security.SecurityFilter;
import com.trifork.sdm.replication.security.SignatureBuilder;
import com.trifork.sdm.replication.security.SignatureBuilder.HTTPMethod;


public class SecurityFilterTest extends ReplicationTest {

	private String secret = "ef_fwefoihe%wu32ew";
	private String username = "gateway";


	@Override
	public void initialize() {

		// The filter is what we are under testing.

		filter("/*").through(SecurityFilter.class);

		bindConstant().annotatedWith(Resource.class).to("/resource");

		// Serve everything through a mock servlet.

		serve("/resource").with(new HttpServlet() {

			private static final long serialVersionUID = 1L;


			@Override
			protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException,
					IOException {

				resp.setStatus(HttpURLConnection.HTTP_OK);
			}
		});

		bindConstant().annotatedWith(Secret.class).to(secret);
	}


	@Test
	@Inject
	public void requests_with_no_signature_should_be_rejected(HttpURLConnection connection)
			throws IOException {

		connection.connect();

		assertThat(connection.getResponseCode(), is(HttpURLConnection.HTTP_FORBIDDEN));
	}


	@Test
	public void request_with_a_signature_that_does_not_match_should_be_rejected(URL baseURL) throws IOException {
		
		SignatureBuilder signatureBuilder = new SignatureBuilder(HTTPMethod.GET,username, secret, "resourceA", getTomorrow());
		
		String resource = String.format("resourceA?username=gateway&signature=");
		
		
		// Invalid Bucket
		
	}
	
	protected long getTomorrow() {
		
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DATE, 1);
		return calendar.getTime().getTime();
	}
}
