package com.trifork.sdm.replication.service;

import javax.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;

public class ResourceDispatcher extends GuiceServletContextListener {

	private final Injector injector;

	@Inject
	ResourceDispatcher(Injector injector) {

		this.injector = injector.createChildInjector();
	}

	@Override
	protected Injector getInjector() {

		return injector;
	}
}
