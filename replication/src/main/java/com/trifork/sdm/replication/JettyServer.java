package com.trifork.sdm.replication;

import javax.servlet.http.HttpServlet;

import org.mortbay.jetty.servlet.Context;

import com.google.inject.name.Named;
import com.google.inject.servlet.GuiceFilter;


public class JettyServer implements Server {

	protected String host;
	protected int port;
	
	protected org.mortbay.jetty.Server server;


	public JettyServer(@Named("Host") String host, @Named("Port") int port) {

		assert host != null;
		assert port >= 0;

		this.host = host;
		this.port = port;
	}


	@Override
	public void start() {

		// Start an embedded Jetty server.

		server = new org.mortbay.jetty.Server(port);
		Context root = new Context(server, "/", Context.NO_SESSIONS);

		// Set up the server to use Guice.
		// This part is equivalent to the web.xml file.

		root.addFilter(GuiceFilter.class, "/*", 0);
		root.addEventListener(new ServerConfiguration());

		// Add a dummy servet to the server, or
		// Jetty won't serve anything at all.

		root.addServlet(HttpServlet.class, "/");

		try {
			server.start();
			server.join();
		}
		catch (Exception e) {
			e.printStackTrace(); // TODO: Handle this exception.
		}
	}


	@Override
	public void stop() {
		
		if (server.isRunning()) {
			try {
				server.stop();
			}
			catch (Exception e) {
				e.printStackTrace(); // TODO: Handle this exception.
			}
		}
	}
}
