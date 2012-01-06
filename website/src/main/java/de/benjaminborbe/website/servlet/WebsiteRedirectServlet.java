package de.benjaminborbe.website.servlet;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public abstract class WebsiteRedirectServlet extends HttpServlet {

	private static final long serialVersionUID = -548691469381588488L;

	protected final Logger logger;

	@Inject
	public WebsiteRedirectServlet(final Logger logger) {
		this.logger = logger;
	}

	@Override
	public void service(final HttpServletRequest request, final HttpServletResponse response) throws IOException {
		logger.debug("service");
		response.sendRedirect(buildRedirectTargetPath(request));
	}

	protected String buildRedirectTargetPath(final HttpServletRequest request) {
		return request.getContextPath() + "/" + getTarget();
	}

	protected abstract String getTarget();
}
