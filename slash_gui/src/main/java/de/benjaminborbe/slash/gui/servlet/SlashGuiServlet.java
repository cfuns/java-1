package de.benjaminborbe.slash.gui.servlet;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class SlashGuiServlet extends HttpServlet {

	private static final long serialVersionUID = 1328676176772634649L;

	private static final String DEFAULT_TARGET = "dashboard";

	private final Logger logger;

	@Inject
	public SlashGuiServlet(final Logger logger) {
		this.logger = logger;
	}

	@Override
	public void service(final HttpServletRequest request, final HttpServletResponse response) throws IOException {
		logger.debug("service");
		response.sendRedirect(buildRedirectTargetPath(request));
	}

	protected String buildRedirectTargetPath(final HttpServletRequest request) {
		return request.getContextPath() + "/" + DEFAULT_TARGET;
	}
}
