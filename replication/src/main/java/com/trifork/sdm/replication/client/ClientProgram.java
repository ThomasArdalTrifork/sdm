package com.trifork.sdm.replication.client;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.stream.XMLStreamException;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.trifork.sdm.replication.configuration.GatewayModule;
import com.trifork.sdm.replication.configuration.DatabaseModule;
import com.trifork.sdm.replication.configuration.ResourceModule;
import com.trifork.sdm.replication.configuration.ServerModule;
import com.trifork.sdm.replication.service.MySQLConnectionFactory;
import com.trifork.sdm.replication.service.RecordPersister;
import com.trifork.sdm.replication.service.Server;

public class ClientProgram {
	
	
}
