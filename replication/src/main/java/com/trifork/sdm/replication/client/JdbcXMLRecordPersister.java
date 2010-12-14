package com.trifork.sdm.replication.client;

import static javax.xml.stream.XMLStreamConstants.END_DOCUMENT;
import static javax.xml.stream.XMLStreamConstants.START_DOCUMENT;

import java.io.InputStream;

import javax.inject.Inject;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import com.trifork.sdm.replication.service.JdbcConnectionFactory;
import com.trifork.sdm.replication.service.RecordPersister;


public class JdbcXMLRecordPersister implements RecordPersister {

	private JdbcConnectionFactory connectionFactory;


	@Inject
	JdbcXMLRecordPersister(JdbcConnectionFactory connectionFactory) {

		this.connectionFactory = connectionFactory;
	}


	@Override
	public boolean persist(InputStream inputStream) throws XMLStreamException {

		XMLInputFactory factory = (XMLInputFactory) XMLInputFactory.newInstance();
		XMLStreamReader staxXmlReader = (XMLStreamReader) factory.createXMLStreamReader(inputStream);

		for (int event = staxXmlReader.next(); event != END_DOCUMENT; event = staxXmlReader.next()) {

			if (event == START_DOCUMENT) {

				String s = staxXmlReader.getLocalName();
			}

			// Method setter = EntityHelper.getSetterFromGetter(entity, getter);

			// staxXmlReader.getLocalName();
		}

		return false;
	}
}
