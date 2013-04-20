package de.benjaminborbe.bookmark.service;

import javax.inject.Inject;
import javax.inject.Singleton;
import org.apache.felix.http.api.ExtHttpService;
import org.osgi.service.http.HttpContext;
import org.osgi.service.http.NamespaceException;

import javax.servlet.Filter;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Map;

@Singleton
public class MyExtHttpService implements ExtHttpService {

	private final ServletPathRegistry registry;

	private final Map<Servlet, String> servletAlias = new HashMap<>();

	@Inject
	public MyExtHttpService(final ServletPathRegistry registry) {
		this.registry = registry;
	}

	@Override
	public void registerServlet(final String alias, final Servlet servlet, @SuppressWarnings("rawtypes") final Dictionary initparams, final HttpContext context)
		throws ServletException, NamespaceException {
		registry.add(alias);
		servletAlias.put(servlet, alias);
	}

	@Override
	public void registerResources(final String alias, final String name, final HttpContext context) throws NamespaceException {
	}

	@Override
	public void unregister(final String alias) {
		registry.remove(alias);
	}

	@Override
	public HttpContext createDefaultHttpContext() {
		return null;
	}

	@Override
	public void registerFilter(final Filter filter, final String pattern, @SuppressWarnings("rawtypes") final Dictionary initParams, final int ranking, final HttpContext context)
		throws ServletException {
	}

	@Override
	public void unregisterFilter(final Filter filter) {
	}

	@Override
	public void unregisterServlet(final Servlet servlet) {
		if (servletAlias.containsKey(servlet)) {
			final String alias = servletAlias.get(servlet);
			unregister(alias);
			servletAlias.remove(servlet);
		}
	}

}
