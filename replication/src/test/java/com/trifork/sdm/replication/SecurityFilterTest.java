package com.trifork.sdm.replication;

import java.io.IOException;
import java.net.HttpURLConnection;

import javax.inject.Inject;

import org.junit.Test;

import com.trifork.sdm.replication.configuration.Resource;
import com.trifork.sdm.replication.security.GatewayServlet;


public class SecurityFilterTest extends ReplicationTest {

	@Override
	public void initialize() {

		final String resource = "gateway";
		
		bindConstant().annotatedWith(Resource.class).to(resource);
		
		serve("/" + resource).with(GatewayServlet.class);
	}


	@Test
	@Inject
	public void requests_with_no_authorization_header_should_be_rejected(HttpURLConnection connection)
			throws IOException {

		connection.connect();
	}


	@Test
	public void request_with_invalid_user_signature_combination_should_be_rejected() {

	}
}
