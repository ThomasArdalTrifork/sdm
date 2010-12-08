package com.trifork.sdm.replication.configuration;

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

import com.google.inject.servlet.ServletModule;
import com.trifork.sdm.persistence.annotations.Output;
import com.trifork.sdm.replication.security.GatewayServlet;
import com.trifork.sdm.replication.security.SecurityFilter;

public class ResourceModule extends ServletModule {

	@Override
	protected void configureServlets() {

		// Security
		// Set up the security gateway.

		serve("/gateway").with(GatewayServlet.class);

		// Find all entities and serve them as resources.

		final String ENTITY_PACKAGE = "com.trifork.sdm.models";

		Reflections reflector = new Reflections(new ConfigurationBuilder()
				.setUrls(ClasspathHelper.getUrlsForPackagePrefix(ENTITY_PACKAGE))
				.filterInputsBy(new FilterBuilder().include(FilterBuilder.prefix(ENTITY_PACKAGE)))
				.setScanners(new TypeAnnotationsScanner()));

		// Serve all entities by deferring their URLs and using their
		// annotations.

		Set<Class<?>> entities = reflector.getTypesAnnotatedWith(Output.class);

		bind(Set.class).toInstance(entities);

		for (Class<?> entity : entities) {

			final String resourcePath = "/" + entity.getSimpleName().toLowerCase();

			//serve(resourcePath).with(new ResourceServlet(entity, ));
			filter(resourcePath).through(SecurityFilter.class);
		}

		// Serve everything else with status 404.

		serve("/*").with(new HttpServlet() {

			private static final long serialVersionUID = 1L;

			@Override
			protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException,
					IOException {

				resp.setStatus(404);
			}
		});
	}
}