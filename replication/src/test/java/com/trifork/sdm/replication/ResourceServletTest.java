package com.trifork.sdm.replication;

import static java.net.HttpURLConnection.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;

import org.junit.Test;

import com.trifork.sdm.models.sor.Apotek;
import com.trifork.sdm.replication.configuration.Bucket;


public class ResourceServletTest extends ReplicationTest {
	
	@Override
	public void initialize() {

		bindConstant().annotatedWith(Bucket.class).to("/apotek");
		
		ConnectionManager manager = new MySQLConnectionManager("sdm_test", "root", "", "jdbc:mysql://localhost/");
		serve("/apotek").with(new ResourceServlet(Apotek.class, manager));
		
		Apotek instance = new Apotek();
		instance.setBynavn("Horsens");
		
		insert(instance);
	}
	
	
	@Test
	public void should_reject_unaccepted_mime_type(URL resource) throws IOException {
		
		Calendar yesterday = Calendar.getInstance();
		yesterday.add(Calendar.DATE, -1);
		
		String yesterdayString = Long.toString(yesterday.getTimeInMillis() / 1000);
		String pid = "0000000000";
		
		String updateToken = yesterdayString + pid;
		
		URL query = new URL(resource, "/apotek?since=" + updateToken);
		
		HttpURLConnection connection = (HttpURLConnection) query.openConnection();
		
		connection.connect();
		
		assertThat(connection.getResponseCode(), is(HTTP_OK));
	}
}
