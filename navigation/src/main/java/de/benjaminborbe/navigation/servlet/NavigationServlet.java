package de.benjaminborbe.navigation.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.navigation.api.NavigationWidget;

@Singleton
public class NavigationServlet extends HttpServlet {

	private static final long serialVersionUID = 1328676176772634649L;

	private final Logger logger;

	private final NavigationWidget navigationWidget;

	@Inject
	public NavigationServlet(final Logger logger, final NavigationWidget navigationWidget) {
		this.logger = logger;
		this.navigationWidget = navigationWidget;
	}

	@Override
	public void service(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
		logger.debug("service");
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html");

		final PrintWriter out = response.getWriter();
		out.println("<h2>Navigation</h2>");
		navigationWidget.render(request, response);
	}

}
