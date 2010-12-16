package com.trifork.sdm.replication.client;

import static javax.xml.stream.XMLStreamConstants.*;

import java.io.InputStream;
import java.sql.Connection;

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
	public String persist(InputStream inputStream) throws XMLStreamException {

		XMLInputFactory factory = (XMLInputFactory) XMLInputFactory.newInstance();
		XMLStreamReader staxXmlReader = (XMLStreamReader) factory.createXMLStreamReader(inputStream);

		Connection connection = connectionFactory.create();

		String offset = null;

		for (int event = staxXmlReader.next(); event != END_DOCUMENT; event = staxXmlReader.next()) {

			if (event == START_ELEMENT) {

				String s = staxXmlReader.getLocalName();
				int i = 0;
			}

			// Method setter = EntityHelper.getSetterFromGetter(entity, getter);

			// staxXmlReader.getLocalName();
		}

		return offset;
	}
}
