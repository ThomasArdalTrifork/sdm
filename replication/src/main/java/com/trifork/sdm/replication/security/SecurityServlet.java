package com.trifork.sdm.replication.security;

import java.io.IOException;
import java.util.Calendar;

import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.trifork.sdm.models.sor.Apotek;


public class SecurityServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private static final int UNDEFINED = -1;

	private final SessionStore sessionStore;
	private final int sessionTTL;
	private final int defaultPageSize;


	@Inject
	public SecurityServlet(SessionStore sessionStore, SecurityModule securityModule,
			@Named("SessionTTL") int sessionTTL, @Named("DefaultPageSize") int defaultPageSize) {

		assert sessionStore != null;
		assert sessionTTL > 0;
		assert defaultPageSize > 0;

		this.sessionStore = sessionStore;
		this.sessionTTL = sessionTTL;
		this.defaultPageSize = defaultPageSize;
	}


	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		// TODO: Authenticate the user.

		// TODO: Pull the request's data from the SOAP envelope.

		final Class<?> entity = Apotek.class;
		final int entityVersion = 1;

		final Calendar since = Calendar.getInstance();
		since.add(Calendar.MONTH, -6);

		int pageSize = UNDEFINED; // TODO: Read pageSize from request.

		if (pageSize == UNDEFINED) pageSize = defaultPageSize;

		// Define the time window where the resource will be accessible.

		// Authorize the user for a time window.

		AuthorizationBuilder builder = new AuthorizationBuilder().setEntity(entity)
				.setEntityVersion(entityVersion).setTTL(sessionTTL);

		String token = sessionStore.createToken(builder.build());

		// Create the response.

		response.setContentType("application/soap+xml");
		response.setStatus(200);

		// FIXME: Don't hardcode the host and port.
		final String templateURL = "https://0.0.0.0:3000/resource/%s?pageSize=%d";
		String resourceURL = String.format(templateURL, token, pageSize);

		// TODO: The above should be written in a DGWS envelope.
		response.getOutputStream().println(resourceURL);
	}
}
