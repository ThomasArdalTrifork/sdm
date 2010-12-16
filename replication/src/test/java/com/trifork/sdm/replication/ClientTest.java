package com.trifork.sdm.replication;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.xml.HasXPath.hasXPath;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.stream.XMLStreamException;

import org.junit.Test;
import org.w3c.dom.Document;

import com.trifork.sdm.models.NamingConvention;
import com.trifork.sdm.models.Record;
import com.trifork.sdm.models.sor.Apotek;
import com.trifork.sdm.replication.client.Client;
import com.trifork.sdm.replication.configuration.GatewayModule;
import com.trifork.sdm.replication.configuration.ResourceModule;


public class ClientTest extends ReplicationTest {

	private static Class<? extends Record> entity = Apotek.class;


	public void initialize() {

		install(new GatewayModule());
		install(new ResourceModule().add(entity));
	}


	@Test
	public void should_behave_correctly_durring_bootstrap(Client client) throws IOException {

		InputStream input = client.replicate(NamingConvention.getResourceName(entity));
		Document document = parse(input, true);

		// Make sure the page borders contain the records we expect.

		assertThat(document, hasXPath("//apotek[1]/@recordId", is("1")));
		assertThat(document, hasXPath("//apotek[24]/@recordId", is("24")));

		// Make sure that we don't have anymore records.

		assertThat(document, not(hasXPath("//apotek[25]")));
	}


	@Test
	public void should_behave_correctly_durring_an_update(Client client) throws IOException {

		String offset = "12920249380000000022";

		InputStream input = client.replicate(NamingConvention.getResourceName(entity), offset);
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
		 * Client client = new Client("0.0.0.0", 3000);
		 * 
		 * for (String resource : resources) {
		 * 
		 * String offset = null; boolean hasMore = true;
		 * 
		 * while (hasMore) {
		 * 
		 * InputStream inputStream = client.replicate(resource, offset); hasMore
		 * = persister.persist(inputStream); } }
		 * 
		 * // TODO: Assert that the contents of the databases are the same.
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
