package com.trifork.sdm.replication;

import com.google.inject.Binder;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.name.Names;


public final class Program {

	public static void main(String[] args) throws Exception {

		if (args.length != 2) {
			
			printUsage();
			System.exit(-1);
		}
		else {
			
			// Parse the command line arguments.
			
			final String host = args[0];
			final int port = Integer.parseInt(args[1]);
			
			// Set up component dependencies. 
			
			Injector injector = Guice.createInjector(new Module() {

				@Override
				public void configure(Binder binder) {
					
					// The server.
					
					binder.bind(Server.class).to(JettyServer.class);
					binder.bindConstant().annotatedWith(Names.named("Host")).to(host);
					binder.bindConstant().annotatedWith(Names.named("Port")).to(port);
					
					// TODO: Failure handling through FailureNotifier.
				}
			});
			
			// Start the server.
			
			Server server = injector.getInstance(Server.class);
			server.run();
			
			System.exit(1);
		}
	}


	private static void printUsage() {

		System.out.println("Usage: replication [HOST] [PORT]");
	}
}
