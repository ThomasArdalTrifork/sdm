package com.trifork.sdm.replication;

import java.io.IOException;
import java.util.Date;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.trifork.sdm.models.Record;
import com.trifork.sdm.replication.configuration.DatabaseModule.QueryFactory;


@Singleton
public class ResourceServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	
	@Inject
	private Map entities;

	@Inject
	private QueryFactory queryFactory;


	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException,
			IOException {

		// Fetch the query parameters.
		
		String token = request.getParameter("token");		
		Date since = new Date(Long.parseLong(token.substring(0, 9)));
		long recordId = Long.parseLong(token.substring(10));
		
		// Figure out which resource has been requested. 
		
		String resource = request.getRequestURI();
		Class<? extends Record> entity = (Class<? extends Record>)entities.get(resource);
		
		// Determine the output format.
		
		EntityWriter writer = new XMLEntityWriter(entity);
		
		// Construct a query.
		
		Query query = queryFactory.create(entity, recordId, since);
		
		// Return the resulting records.
		
		writer.output(query, response.getOutputStream());
	}
}
