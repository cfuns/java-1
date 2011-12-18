package de.benjaminborbe.tools.osgi.mock;

import java.util.Dictionary;

import javax.servlet.Filter;
import javax.servlet.Servlet;
import javax.servlet.ServletException;

import org.apache.felix.http.api.ExtHttpService;
import org.osgi.service.http.HttpContext;
import org.osgi.service.http.NamespaceException;

public class ExtHttpServiceMock implements ExtHttpService {

	@Override
	public void registerServlet(final String alias, final Servlet servlet,
			@SuppressWarnings("rawtypes") final Dictionary initparams, final HttpContext context) throws ServletException,
			NamespaceException {
		

	}

	@Override
	public void registerResources(final String alias, final String name, final HttpContext context)
			throws NamespaceException {
		

	}

	@Override
	public void unregister(final String alias) {
		

	}

	@Override
	public HttpContext createDefaultHttpContext() {
		
		return null;
	}

	@Override
	public void registerFilter(final Filter filter, final String pattern,
			@SuppressWarnings("rawtypes") final Dictionary initParams, final int ranking, final HttpContext context)
			throws ServletException {
		

	}

	@Override
	public void unregisterFilter(final Filter filter) {
		

	}

	@Override
	public void unregisterServlet(final Servlet servlet) {
		

	}

}
