package com.trifork.sdm.replication;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import javax.xml.stream.XMLStreamException;

import org.junit.Test;

import com.trifork.sdm.replication.client.Client;
import com.trifork.sdm.replication.client.JdbcXMLRecordPersister;
import com.trifork.sdm.replication.configuration.ClientModule;
import com.trifork.sdm.replication.configuration.DatabaseModule;
import com.trifork.sdm.replication.configuration.GatewayModule;
import com.trifork.sdm.replication.configuration.ResourceModule;


public class IntegrationTest extends ReplicationTest {

	public void initialize() {

		install(new ClientModule());
		install(new DatabaseModule());
		
		install(new GatewayModule());
		install(new ResourceModule().serveAllEntities());
	}


	@Test
	public void replicated_db_should_match_the_original_completly(Client client, JdbcXMLRecordPersister persister, Map routes) throws IOException, XMLStreamException {

		// Replicate the SDM's database, by pulling
		// down the resources one by one.

		for (Object key : routes.keySet()) {

			// The routes start with a slash '/'. We need to remove that
			// to get the resource name.
			
			String resource = ((String) key).substring(1);
			
			// Keep replicating until we don't get a new URL.
			
			String offset = null;
			String url = null;
			
			do {
				
				InputStream inputStream = client.replicate(resource, offset);
				url = persister.persist(inputStream);
			}
			while (url != null);
		}

		// TODO: Assert that the contents of the databases are the same.
	}
}
