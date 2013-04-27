package de.benjaminborbe.website.servlet;

import org.slf4j.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Singleton
public class RedirectUtil {

	private final Logger logger;

	@Inject
	public RedirectUtil(final Logger logger) {
		this.logger = logger;
	}

	public void sendRedirect(final HttpServletResponse response, final String target) throws IOException {
		logger.trace("send redirect to " + target);
		response.sendRedirect(target);
	}
}
