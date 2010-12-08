package com.trifork.sdm.replication;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import org.apache.commons.io.IOUtils;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;

import com.google.inject.Provides;
import com.google.inject.servlet.ServletModule;
import com.trifork.sdm.models.sor.Apotek;
import com.trifork.sdm.persistence.annotations.Output;
import com.trifork.sdm.replication.Server;
import com.trifork.sdm.replication.configuration.Bucket;
import com.trifork.sdm.replication.configuration.properties.Host;
import com.trifork.sdm.replication.configuration.properties.Port;
import com.trifork.sdm.replication.junit.TestRunner;


@RunWith(TestRunner.class)
public abstract class ReplicationTest extends ServletModule {

	private static Server server;
	private final Set<Object> fixtures = new HashSet<Object>();

	private static ConnectionManager manager;


	@Override
	public final void configureServlets() {

		bindConstant().annotatedWith(Host.class).to("0.0.0.0");
		bindConstant().annotatedWith(Port.class).to(3001);

		initialize();
	}


	public abstract void initialize();


	protected void insert(Object fixture) {

		Output typeAnnotation = fixture.getClass().getAnnotation(Output.class);
		assert typeAnnotation != null : "Inserted fixtures must be annotated with @Output.";
		
		String tableName = !typeAnnotation.name().isEmpty() ? typeAnnotation.name() : fixture.getClass().getSimpleName();
		
		fixtures.add(fixture);
	}


	private void delete() {

	}


	@Inject
	@Provides
	public URL provideURL(@Host String host, @Port int port, @Bucket String resource) throws IOException {

		return new URL("http", host, port, resource);
	}


	@Inject
	@Provides
	public HttpURLConnection provideConnection(URL server) throws IOException {

		HttpURLConnection connection = (HttpURLConnection) server.openConnection();

		connection.setUseCaches(false);
		connection.setDoOutput(true);

		return connection;
	}


	@BeforeClass
	public static void start(Server server, ConnectionManager manager) {

		ReplicationTest.manager = manager;
		
		ReplicationTest.server = server;
		new Thread(server).run();
	}


	@AfterClass
	public static void finish() {

		server.stop();
	}


	protected String readInputStream(URLConnection connection) throws IOException {

		return IOUtils.toString(connection.getInputStream(), "UTF-8");
	}
}
