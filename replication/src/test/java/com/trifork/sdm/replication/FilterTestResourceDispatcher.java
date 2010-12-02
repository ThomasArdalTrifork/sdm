package com.trifork.sdm.replication;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;
import com.google.inject.servlet.ServletModule;
import com.trifork.sdm.replication.security.SecurityFilter;

public class FilterTestResourceDispatcher extends GuiceServletContextListener {

	public static final String RESPONSE_STRING = "RESPONSE STRING";

	@Override
	protected Injector getInjector() {

		return Guice.createInjector(new ServletModule() {

			@Override
			protected void configureServlets() {

				filter("/*").through(SecurityFilter.class);

				serve("/*").with(new HttpServlet() {
					private static final long serialVersionUID = 1L;

					@Override
					protected void doGet(HttpServletRequest req, HttpServletResponse resp)
							throws ServletException, IOException {
						
						resp.getOutputStream().print(RESPONSE_STRING);
					}
				});
			}
		});
	}

}
