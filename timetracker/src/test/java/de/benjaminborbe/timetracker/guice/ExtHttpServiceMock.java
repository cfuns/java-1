package de.benjaminborbe.timetracker.guice;

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
		// TODO Auto-generated method stub

	}

	@Override
	public void registerResources(final String alias, final String name, final HttpContext context)
			throws NamespaceException {
		// TODO Auto-generated method stub

	}

	@Override
	public void unregister(final String alias) {
		// TODO Auto-generated method stub

	}

	@Override
	public HttpContext createDefaultHttpContext() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void registerFilter(final Filter filter, final String pattern,
			@SuppressWarnings("rawtypes") final Dictionary initParams, final int ranking, final HttpContext context)
			throws ServletException {
		// TODO Auto-generated method stub

	}

	@Override
	public void unregisterFilter(final Filter filter) {
		// TODO Auto-generated method stub

	}

	@Override
	public void unregisterServlet(final Servlet servlet) {
		// TODO Auto-generated method stub

	}

}
