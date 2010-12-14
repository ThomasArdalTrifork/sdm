package com.trifork.sdm.replication.service;

import java.util.EventListener;

import javax.inject.Inject;
import javax.servlet.http.HttpServlet;

import org.mortbay.jetty.servlet.Context;

import com.google.inject.servlet.GuiceFilter;
import com.trifork.sdm.replication.configuration.properties.Host;
import com.trifork.sdm.replication.configuration.properties.Port;


public class JettyServer implements Server {

	protected String host;
	protected int port;

	protected org.mortbay.jetty.Server server;
	private final EventListener dispatcher;

	@Inject
	JettyServer(@Host String host, @Port int port, EventListener dispatcher) {

		assert host != null;
		assert port >= 0;
		assert dispatcher != null;

		this.host = host;
		this.port = port;
		this.dispatcher = dispatcher;
	}


	@Override
	public void run() {

		// Start an embedded Jetty server.

		server = new org.mortbay.jetty.Server(port);

		// TODO: Set the servers host.

		Context root = new Context(server, "/", Context.NO_SESSIONS);

		// Set up the server to use Guice.
		// This part is equivalent to the web.xml file.

		root.addFilter(GuiceFilter.class, "/*", 0);
		root.addEventListener(dispatcher);

		// Add a dummy servet to the server, or
		// Jetty won't serve anything at all.

		root.addServlet(HttpServlet.class, "/");

		try {
			server.start();
		}
		catch (Exception e) {
			e.printStackTrace(); // TODO: Handle this exception.
		}
	}


	@Override
	public void stop() {

		if (server != null && server.isRunning()) {
			try {
				server.stop();
			}
			catch (Exception e) {
				e.printStackTrace(); // TODO: Handle this exception.
			}
			finally {
				server = null;
			}
		}
	}
}
