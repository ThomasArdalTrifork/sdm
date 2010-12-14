package com.trifork.sdm.replication.service;

import java.io.InputStream;

import javax.xml.stream.XMLStreamException;



public interface RecordPersister {

	boolean persist(InputStream inputStream) throws XMLStreamException;
}
