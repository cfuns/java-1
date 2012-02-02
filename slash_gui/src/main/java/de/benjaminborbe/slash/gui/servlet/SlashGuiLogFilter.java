package de.benjaminborbe.slash.gui.servlet;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class SlashGuiLogFilter implements Filter {

	private final Logger logger;

	@Inject
	public SlashGuiLogFilter(final Logger logger) {
		this.logger = logger;
	}

	@Override
	public void doFilter(final ServletRequest servletRequest, final ServletResponse servletResponse, final FilterChain filterChain) throws IOException, ServletException {
		try {
			// log cookies
			if (servletRequest instanceof HttpServletRequest) {
				final HttpServletRequest request = (HttpServletRequest) servletRequest;
				final String requestUrl = request.getRequestURL().toString();
				logger.debug("requestUrl: " + requestUrl);
			}
		}
		finally {
			filterChain.doFilter(servletRequest, servletResponse);
		}
	}

	@Override
	public void init(final FilterConfig filterConfig) throws ServletException {
	}

	@Override
	public void destroy() {
	}

}
