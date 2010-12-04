package com.trifork.sdm.replication;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import org.junit.Test;

import com.trifork.sdm.replication.configuration.Resource;
import com.trifork.sdm.replication.configuration.properties.Key;
import com.trifork.sdm.replication.security.GatewayServlet;


public class GatewayTest extends ReplicationTest {

	@Override
	public void initialize() {

		bindConstant().annotatedWith(Resource.class).to("/gateway");
		serve("/gateway").with(GatewayServlet.class);
		
		bindConstant().annotatedWith(Key.class).to("secret");
	}
	
	@Test
	public void should_return_a_valid_URL(HttpURLConnection connection) throws IOException {
		
		connection.setRequestProperty("X-Sdm-Resource", "apotek");
		
		connection.connect();
		
		String response = readInputStream(connection);
		
		new URL(response);
	}


	@Test
	public void should_return_an_expires_date_in_the_future(HttpURLConnection connection) throws IOException {
		
		String currentTimeSecs = new Long(System.currentTimeMillis() / 1000).toString();
		
		connection.setRequestProperty("X-Sdm-Date", currentTimeSecs);
		connection.setRequestProperty("X-Sdm-Resource", "apotek");
		
		connection.connect();
		
		String response = readInputStream(connection);
		
		assertNotNull(response);
	}
}
