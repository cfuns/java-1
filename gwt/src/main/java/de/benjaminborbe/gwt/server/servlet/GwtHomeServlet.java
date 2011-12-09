package de.benjaminborbe.gwt.server.servlet;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class GwtHomeServlet extends ResourceServlet {

	private static final long serialVersionUID = -2162585976374394940L;

	@Inject
	public GwtHomeServlet(final Logger logger) {
		super(logger);
	}

	@Override
	public String getPath() {
		return "/Home.html";
	}

	@Override
	public String contentType() {
		return "text/html";
	}
}
