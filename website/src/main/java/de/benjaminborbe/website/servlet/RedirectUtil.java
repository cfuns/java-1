package de.benjaminborbe.website.servlet;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class RedirectUtil {

	private final Logger logger;

	@Inject
	public RedirectUtil(final Logger logger) {
		this.logger = logger;
	}

	public void sendRedirect(final HttpServletRequest request, final HttpServletResponse response, final String target) throws IOException {
		logger.trace("send redirect to " + target);
		response.sendRedirect(target);
	}
}
