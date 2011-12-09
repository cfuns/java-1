package de.benjaminborbe.gwt.server.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class GwtServlet extends HttpServlet {

	private static final long serialVersionUID = -2162585976374394940L;

	private final Logger logger;

	@Inject
	public GwtServlet(final Logger logger) {
		this.logger = logger;
	}

	@Override
	public void service(final HttpServletRequest request, final HttpServletResponse response) throws ServletException,
			IOException {
		logger.debug("service");
		response.setContentType("text/html");
		final PrintWriter out = response.getWriter();
		out.println("gwt");
	}
}
