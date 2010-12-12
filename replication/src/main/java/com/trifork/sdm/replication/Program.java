package com.trifork.sdm.replication;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.trifork.sdm.replication.configuration.ConfigurationModule;
import com.trifork.sdm.replication.configuration.DatabaseModule;
import com.trifork.sdm.replication.configuration.ResourceModule;
import com.trifork.sdm.replication.configuration.ServerModule;

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

			Injector injector = Guice.createInjector(new ConfigurationModule(), new ResourceModule(),
					new ServerModule(host, port), new DatabaseModule());

			// Start the server.

			Server server = injector.getInstance(Server.class);
			
			Thread runner = new Thread(server);
			runner.run();
			Thread.currentThread().join();
			
			System.exit(1);
		}
	}

	private static void printUsage() {

		System.out.println("Usage: replication [HOST] [PORT]");
	}
}
