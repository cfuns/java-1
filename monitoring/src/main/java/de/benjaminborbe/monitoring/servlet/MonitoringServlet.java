package de.benjaminborbe.monitoring.servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.monitoring.check.Check;
import de.benjaminborbe.monitoring.check.CheckRegistry;
import de.benjaminborbe.tools.io.FlushPrintWriter;

@Singleton
public class MonitoringServlet extends HttpServlet {

	private static final long serialVersionUID = 1328676176772634649L;

	private final Logger logger;

	private final CheckRegistry checkRegistry;

	@Inject
	public MonitoringServlet(final Logger logger, final CheckRegistry checkRegistry) {
		this.logger = logger;
		this.checkRegistry = checkRegistry;
	}

	@Override
	public void service(final HttpServletRequest request, final HttpServletResponse response) throws ServletException,
			IOException {
		logger.debug("service");
		response.setContentType("text/plain");
		final FlushPrintWriter out = new FlushPrintWriter(response.getWriter());
		out.println("monitoring checks started");
		for (final Check check : checkRegistry.getAll()) {
			out.println((check.check() ? "[OK] " : "[FAIL] ") + check.getMessage());
		}
		out.println("monitoring checks finished");
	}
}
