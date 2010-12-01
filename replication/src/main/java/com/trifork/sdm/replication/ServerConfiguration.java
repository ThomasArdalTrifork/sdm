package com.trifork.sdm.replication;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.reflections.Reflections;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.name.Names;
import com.google.inject.servlet.GuiceServletContextListener;
import com.google.inject.servlet.ServletModule;
import com.trifork.sdm.persistence.annotations.Output;
import com.trifork.sdm.replication.security.SecurityServlet;


public class ServerConfiguration extends GuiceServletContextListener {

	@Override
	protected Injector getInjector() {
		
		return Guice.createInjector(new ServletModule() {
			
			@Override
		    protected void configureServlets() {
				
				// Configuration
				
				Properties properties = loadProperties();
				
				bindConstant().annotatedWith(Names.named("PrivateKeyFile")).to(properties.getProperty("security.certificate.path"));
				bindConstant().annotatedWith(Names.named("PrivateKeyPassword")).to(properties.getProperty("security.certificate.password"));
				
				// Set up the security gateway.
				// This servlet generates authentication tokens for the user.
				
				serve("/authorize").with(SecurityServlet.class);
				
				
				// Find all entities.
				
				final String ENTITY_PACKAGE = "com.trifork.sdm.models";
				
				Reflections reflector = new Reflections(new ConfigurationBuilder()
					.setUrls(ClasspathHelper.getUrlsForPackagePrefix(ENTITY_PACKAGE))
					.filterInputsBy(new FilterBuilder().include(FilterBuilder.prefix(ENTITY_PACKAGE)))
					.setScanners(new TypeAnnotationsScanner())
				);
				
				
				// Serve all entities by deferring their URLs and using their
				// annotations.
				
				Set<Class<?>> entities = reflector.getTypesAnnotatedWith(Output.class);
				
				for (final Class<?> entity : entities) {
					
					final String path = String.format("/%s", entity.getSimpleName().toLowerCase());
					
					serve(path).with(new EntityServlet(entity));
				}
				
				
				// Serve everything else with status 404.
				
				serve("/*").with(new HttpServlet() {

					private static final long serialVersionUID = 1L;
					
					@Override
					protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
					
						resp.setStatus(404);
					}
				});
			}
			
			private Properties loadProperties() {
				
				// Load properties from the 'config.properties' file.
				
				Properties properties = new Properties();
				InputStream inputStream = null;
				
				try {
					inputStream = getClass().getClassLoader().getResourceAsStream("config.properties");
					properties.load(inputStream);
					inputStream.close();
				}
				catch (Throwable t) {
					throw new RuntimeException("Could not read the 'config.properties' file.", t);
				}
				
				return properties;
			}
		});
	}
}
