package com.trifork.sdm.replication.configuration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.Entity;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.reflections.Reflections;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;

import com.google.inject.servlet.ServletModule;
import com.trifork.sdm.models.Record;
import com.trifork.sdm.replication.service.GatewayServlet;
import com.trifork.sdm.replication.service.ResourceServlet;
import com.trifork.sdm.replication.service.SecurityFilter;


public class ResourceModule extends ServletModule implements Iterable<Class<? extends Record>>{

	private Set<Class<? extends Record>> resources = new HashSet<Class<? extends Record>>();


	@Override
	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected void configureServlets() {

		// Security
		// Set up the security gateway.

		serve("/gateway").with(GatewayServlet.class);

		Map entityMap = new HashMap<String, Class>();

		for (Class<?> entity : resources) {
			final String resourcePath = "/" + entity.getSimpleName().toLowerCase();

			serve(resourcePath).with(ResourceServlet.class);
			filter(resourcePath).through(SecurityFilter.class);

			entityMap.put(resourcePath, entity);
		}

		bind(Map.class).toInstance(entityMap);

		// Serve everything else with status 404.
		// TODO: Is this really the best way? Can't the server handle this?

		serve("/*").with(new HttpServlet() {

			private static final long serialVersionUID = 1L;


			@Override
			protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

				resp.setStatus(404);
			}
		});
	}


	public ResourceModule add(Class<? extends Record> entity) {

		resources.add(entity);

		return this;
	}


	@SuppressWarnings("unchecked")
	public ResourceModule serveAllEntities() {

		// Find all entities and serve them as resources.

		final String ENTITY_PACKAGE = "com.trifork.sdm.models";

		Reflections reflector = new Reflections(new ConfigurationBuilder().setUrls(ClasspathHelper.getUrlsForPackagePrefix(ENTITY_PACKAGE)).filterInputsBy(new FilterBuilder().include(FilterBuilder.prefix(ENTITY_PACKAGE))).setScanners(new TypeAnnotationsScanner()));

		// Serve all entities by deferring their URLs and using their
		// annotations.

		Set<Class<?>> entities = reflector.getTypesAnnotatedWith(Entity.class);

		for (Class<?> entity : entities) {
			resources.add((Class<? extends Record>) entity);
		}

		return this;
	}


	@Override
	public Iterator<Class<? extends Record>> iterator() {

		return resources.iterator();
	}
}