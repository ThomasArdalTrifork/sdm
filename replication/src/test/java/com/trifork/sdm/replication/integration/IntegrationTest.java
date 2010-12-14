package com.trifork.sdm.replication.integration;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.stream.XMLStreamException;

import org.junit.Test;

import com.google.inject.Module;
import com.trifork.sdm.models.Record;
import com.trifork.sdm.models.sor.Apotek;
import com.trifork.sdm.replication.client.Client;
import com.trifork.sdm.replication.configuration.GatewayModule;
import com.trifork.sdm.replication.configuration.ResourceModule;


public class IntegrationTest {

	private ResourceModule resources;


	public Module[] getConfiguration() {

		resources = new ResourceModule().addAll();

		return new Module[] { new GatewayModule(), resources };
	}


	@Test
	public void replicated_db_should_match_the_original_completly() throws IOException, XMLStreamException {

		// Replicate the servers database.

		Client client = new Client("0.0.0.0", 3000);
		/*
		for (Class<? extends Record> resource : resources) {

			String offset = null;
			boolean hasMore = true;

			while (hasMore) {
				
				InputStream inputStream = client.replicate(resource, offset);
				hasMore = persister.persist(inputStream);
				
			}
		}
	*/
		// TODO: Assert that the contents of the databases are the same.
	}
}
