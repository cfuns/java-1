package de.benjaminborbe.poker.test;

import org.apache.felix.http.api.ExtHttpService;
import org.osgi.service.http.HttpContext;
import org.osgi.service.http.NamespaceException;

import javax.servlet.Filter;
import javax.servlet.Servlet;
import javax.servlet.ServletException;

import java.util.Dictionary;
import java.util.HashMap;
import java.util.Map;

public class ExtHttpServiceHelper implements ExtHttpService {

	private final Map<String, Servlet> servlets = new HashMap<String, Servlet>();

	@SuppressWarnings("rawtypes")
	@Override
	public void registerFilter(final Filter filter, final String pattern, final Dictionary initParams, final int ranking, final HttpContext context) throws ServletException {
	}

	@Override
	public void unregisterFilter(final Filter filter) {
	}

	@Override
	public void unregisterServlet(final Servlet servlet) {
		String key = null;
		for (final Map.Entry<String, Servlet> s : servlets.entrySet()) {
			if (s.getValue().equals(servlet)) {
				key = s.getKey();
			}
		}
		if (key != null) {
			servlets.remove(key);
		}
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void registerServlet(final String path, final Servlet servlet, final Dictionary dictionary, final HttpContext httpContext) throws ServletException, NamespaceException {
		servlets.put(path, servlet);
	}

	@Override
	public void registerResources(final String s, final String s2, final HttpContext httpContext) throws NamespaceException {
	}

	@Override
	public void unregister(final String s) {
		servlets.remove(s);
	}

	@Override
	public HttpContext createDefaultHttpContext() {
		return null;
	}

	public Servlet getServlet(final String path) {
		return servlets.get(path);
	}
}
