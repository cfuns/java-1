package de.benjaminborbe.tools.http;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;

import javax.inject.Inject;

public abstract class HttpFilter implements Filter {

	protected final Logger logger;

	@Inject
	public HttpFilter(final Logger logger) {
		this.logger = logger;
	}

	@Override
	public void init(final FilterConfig filterConfig) throws ServletException {
	}

	@Override
	public void doFilter(final ServletRequest servletRequest, final ServletResponse servletResponse, final FilterChain filterChain) throws IOException, ServletException {
		logger.trace("doFilter");
		if (servletRequest instanceof HttpServletRequest && servletResponse instanceof HttpServletResponse) {
			final HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
			final HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;
			doFilter(httpServletRequest, httpServletResponse, filterChain);
		}
		else {
			filterChain.doFilter(servletRequest, servletResponse);
		}
	}

	public abstract void doFilter(final HttpServletRequest httpServletRequest, final HttpServletResponse httpServletResponse, final FilterChain filterChain) throws IOException,
			ServletException;

	@Override
	public void destroy() {
	}

}
