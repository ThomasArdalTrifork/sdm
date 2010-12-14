package com.trifork.sdm.replication.integration;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.xml.HasXPath.hasXPath;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.stream.XMLStreamException;

import org.junit.Test;
import org.w3c.dom.Document;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.trifork.sdm.models.Record;
import com.trifork.sdm.models.sor.Apotek;
import com.trifork.sdm.replication.ReplicationTest;
import com.trifork.sdm.replication.client.Client;
import com.trifork.sdm.replication.client.JdbcXMLRecordPersister;
import com.trifork.sdm.replication.configuration.GatewayModule;
import com.trifork.sdm.replication.configuration.DatabaseModule;
import com.trifork.sdm.replication.configuration.ResourceModule;
import com.trifork.sdm.replication.configuration.ServerModule;
import com.trifork.sdm.replication.configuration.properties.Secret;
import com.trifork.sdm.replication.service.GatewayServlet;
import com.trifork.sdm.replication.service.RecordPersister;
import com.trifork.sdm.replication.service.ResourceServlet;
import com.trifork.sdm.replication.service.SecurityFilter;
import com.trifork.sdm.replication.service.Server;


public class ClientTest extends ReplicationTest {

	private static Client client;
	private static String resource = "apotek";


	public void initialize() {

		bindConstant().annotatedWith(Secret.class).to("weeswfwe23");

		serve("/gateway").with(GatewayServlet.class);

		Map entities = new HashMap<String, Class<? extends Record>>();
		entities.put("/apotek", Apotek.class);
		bind(Map.class).toInstance(entities);
		serve("/apotek").with(ResourceServlet.class);

		filter("/apotek").through(SecurityFilter.class);

		// Initialize the client.

		try {
			client = new Client("0.0.0.0", 3001);
		}
		catch (MalformedURLException e) {

			throw new RuntimeException(e);
		}
	}


	@Test
	public void should_behave_correctly_durring_bootstrap() throws IOException {

		InputStream input = client.replicate(resource);
		Document document = parse(input, true);

		// Make sure the page borders contain the records we expect.

		assertThat(document, hasXPath("//apotek[1]/@recordId", is("1")));
		assertThat(document, hasXPath("//apotek[24]/@recordId", is("24")));

		// Make sure that we don't have anymore records.

		assertThat(document, not(hasXPath("//apotek[25]")));
	}


	@Test
	public void should_behave_correctly_durring_an_update() throws IOException {

		String offset = "12920249380000000022";

		InputStream input = client.replicate(resource, offset);
		Document document = parse(input, true);

		// TODO: Change some validTo.

		// Make sure that we get the expected records out.

		assertThat(document, hasXPath("//apotek[1]/@recordId", is("23")));
		assertThat(document, hasXPath("//apotek[2]/@recordId", is("24")));

		// Make sure that we don't get any more records.

		assertThat(document, not(hasXPath("//apotek[3]")));
	}


	@Test
	public void replicated_db_should_match_the_original() throws IOException, XMLStreamException {

		// Replicate the servers database.
		/*
		Client client = new Client("0.0.0.0", 3000);
		
		for (String resource : resources) {

			String offset = null;
			boolean hasMore = true;

			while (hasMore) {

				InputStream inputStream = client.replicate(resource, offset);
				hasMore = persister.persist(inputStream);
			}
		}

		// TODO: Assert that the contents of the databases are the same.
		 
		 */
	}


	//
	// HELPER METHODS
	//

	DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();


	private Document parse(InputStream input, boolean failOnError) {

		Document document;

		try {
			DocumentBuilder builder = documentFactory.newDocumentBuilder();
			document = builder.parse(input);
		}
		catch (Throwable t) {

			if (failOnError) {
				fail(t.getStackTrace().toString());
			}

			document = null;
		}

		return document;
	}
}
