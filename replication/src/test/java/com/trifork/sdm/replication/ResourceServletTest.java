package com.trifork.sdm.replication;

import static com.trifork.sdm.replication.ReplicationTest.Day.YESTERDAY;
import static java.net.HttpURLConnection.HTTP_OK;

import java.io.IOException;
import java.net.URL;

import org.junit.Test;

import com.google.inject.Module;


public class ResourceServletTest extends ReplicationTest {

	@Override
	public void initialize() {

		// serve("/resource").with(servlet(Resource.class));
	}


	@Override
	public Module[] getConfiguration() {

		return new Module[] { this };
	}


	@Test
	public void should_reject_unaccepted_mime_type(URL resource) throws IOException {

		setParam("since", date(YESTERDAY));

		get("/resource");

		assertStatus(HTTP_OK);

		printContent();

		// assertXML("/resourceCollection/resource", is(not(null)));
	}
}
