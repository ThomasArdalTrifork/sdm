package com.trifork.sdm.replication;

import java.io.IOException;

import javax.inject.Singleton;
import javax.persistence.Entity;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.trifork.sdm.Versioned;
import com.trifork.sdm.replication.UpdateQueryBuilder.UpdateQuery;
import com.trifork.sdm.replication.persistence.EntityRepository;


/**
 * Serve an entity using it's {@link Entity} annotations.
 * 
 * Given an entity class it defines a number of default queries using
 * reflection. Output templates are cached to improve performance. You can
 * specialize the handled queries for a particular entity by extending this
 * class.
 * 
 * Only versions marked as supported in the {@link Versioned} annotations will
 * be served.
 */
@Singleton
public class ResourceServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private final EntityWriter writer;
	private final EntityRepository repository;


	public ResourceServlet(Class<?> entity, ConnectionManager connectionManager) {

		assert entity.isAnnotationPresent(Entity.class);

		this.writer = new XMLEntityWriter(entity);
		this.repository = new EntityRepository(entity, connectionManager);
	}


	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException,
			IOException {

		String token = request.getParameter("since");

		UpdateQueryBuilder builder = new UpdateQueryBuilder(token);

		UpdateQuery query = builder.build();

		repository.writeAll(query, 5, writer, response.getOutputStream());
	}
}
