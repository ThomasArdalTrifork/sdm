package com.trifork.sdm.replication;

import java.io.IOException;
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
import com.google.inject.servlet.GuiceServletContextListener;
import com.google.inject.servlet.ServletModule;
import com.trifork.sdm.persistence.annotations.Output;


public class ServerConfiguration extends GuiceServletContextListener {

	@Override
	protected Injector getInjector() {
		
		return Guice.createInjector(new ServletModule() {
			
			@Override
		    protected void configureServlets() {
				
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
					
					serve(path).with(new EntityServlet(entity, 1));
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
		});
	}
}
