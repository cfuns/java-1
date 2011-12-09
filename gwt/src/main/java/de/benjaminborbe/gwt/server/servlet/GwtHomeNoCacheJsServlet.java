package de.benjaminborbe.gwt.server.servlet;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class GwtHomeNoCacheJsServlet extends ResourceServlet {

	private static final long serialVersionUID = -2162585976374394940L;

	@Inject
	public GwtHomeNoCacheJsServlet(final Logger logger) {
		super(logger);
	}

	@Override
	public String getPath() {
		return "/Home/Home.nocache.js";
	}

	@Override
	public String contentType() {
		return "text/javascript";
	}
}
