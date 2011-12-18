package de.benjaminborbe.slash.test;

import java.util.Dictionary;

import javax.servlet.Filter;
import javax.servlet.Servlet;
import javax.servlet.ServletException;

import org.apache.felix.http.api.ExtHttpService;
import org.osgi.service.http.HttpContext;
import org.osgi.service.http.NamespaceException;

public class ExtHttpServiceMock implements ExtHttpService {

	public int registerFilterCallCounter = 0;

	@SuppressWarnings("rawtypes")
	public void registerFilter(final Filter arg0, final String arg1, final Dictionary arg2, final int arg3,
			final HttpContext arg4) throws ServletException {
		registerFilterCallCounter++;
	}

	public int unregisterFilterCallCounter = 0;

	public void unregisterFilter(final Filter arg0) {
		unregisterFilterCallCounter++;
	}

	public int unregisterServletCallCounter = 0;

	public void unregisterServlet(final Servlet arg0) {
		unregisterServletCallCounter++;
	}

	public int createDefaultHttpContextCallCounter = 0;

	public HttpContext createDefaultHttpContext() {
		createDefaultHttpContextCallCounter++;
		return null;
	}

	public int registerResourcesCallCounter = 0;

	public void registerResources(final String alias, final String name, final HttpContext context)
			throws NamespaceException {
		registerResourcesCallCounter++;
	}

	public int registerServletCallCounter = 0;

	@SuppressWarnings("rawtypes")
	public void registerServlet(final String alias, final Servlet servlet, final Dictionary initparams,
			final HttpContext context) throws ServletException, NamespaceException {
		registerServletCallCounter++;
	}

	public int unregisterCallCounter = 0;

	public void unregister(final String alias) {
		unregisterCallCounter++;
	}
}
