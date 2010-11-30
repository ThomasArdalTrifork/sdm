package com.trifork.sdm.replication;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.trifork.sdm.models.sor.Apotek;
import com.trifork.sdm.persistence.annotations.Output;


/**
 * Serve an entity using it's {@link Output} annotations.
 * 
 * Given an entity class it defines a number of default queries using
 * reflection. Output templates are cached to improve performance. You can
 * specialize the handled queries for a particular entity by extending this
 * class.
 * 
 * Only versions marked as supported in the {@link Output} annotations will 
 * be served.
 */
public class EntityServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private EntityWriter writer;

	
	public EntityServlet(Class<?> entity) {

		this.writer = new XMLEntityWriter(entity);
	}


	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		Apotek a = new Apotek();
		a.setApotekNummer(123l);
		a.setCvr(1234512l);
		a.setEmail("thomas@borlum.dk");

		writer.write(a, resp.getOutputStream());

		resp.setContentType(writer.getContentType());
	}
}
