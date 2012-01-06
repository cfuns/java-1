package de.benjaminborbe.website.servlet;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public abstract class WebsiteTextServlet extends HttpServlet {

	private static final long serialVersionUID = 5575454149618155404L;

	private final Logger logger;

	@Inject
	public WebsiteTextServlet(final Logger logger) {
		this.logger = logger;
	}

	@Override
	public void service(final HttpServletRequest request, final HttpServletResponse response) throws IOException {
		logger.debug("service");
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/plain");
		printContent(request, response);
	}

	protected abstract void printContent(final HttpServletRequest request, final HttpServletResponse response) throws IOException;
}
