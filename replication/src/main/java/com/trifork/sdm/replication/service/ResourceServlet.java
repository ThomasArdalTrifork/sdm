package com.trifork.sdm.replication.service;

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
import com.trifork.sdm.util.DateUtils;


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
		
		Date sinceDate;
		long sinceId;
		
		if (token != null) {
			// We don't know exactly how long the token string is so we have the
			// PID offset on the strings' lengths.
			String sinceDateStr = token.substring(0, token.length() - 10);
			sinceDate = new Date(Long.parseLong(sinceDateStr) * 1000);
			
			String sinceIdStr = token.substring(token.length() - 9);
			sinceId = Long.parseLong(sinceIdStr);
		}
		else {
			sinceDate = DateUtils.PAST;
			sinceId = 0;
		}
		
		// Figure out which resource has been requested. 
		
		String resource = request.getRequestURI();
		Class<? extends Record> entity = (Class<? extends Record>)entities.get(resource);
		
		// Determine the output format.
		
		EntitySerializer writer = new XMLEntitySerializer(entity);
		
		// Construct a query.
		
		Query query = queryFactory.create(entity, sinceId, sinceDate);
		
		// Return the resulting records.
		
		writer.output(query, response.getOutputStream());
	}
}
