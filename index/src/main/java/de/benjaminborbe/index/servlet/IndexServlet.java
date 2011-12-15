package de.benjaminborbe.index.servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class IndexServlet extends HttpServlet {

	private static final long serialVersionUID = 1328676176772634649L;

	private final Logger logger;

	@Inject
	public IndexServlet(final Logger logger) {
		this.logger = logger;
	}

	@Override
	public void service(final HttpServletRequest request, final HttpServletResponse response) throws ServletException,
			IOException {
		logger.debug("service");
		// response.setContentType("text/html");
		// final PrintWriter out = response.getWriter();
		// out.println("/");
		final String target = request.getContextPath() + "/search";
		logger.debug("redirect to target: " + target);
		response.sendRedirect(target);
	}
}
