package de.benjaminborbe.tools.osgi.mock;

import java.util.Dictionary;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.Servlet;
import javax.servlet.ServletException;

import org.apache.felix.http.api.ExtHttpService;
import org.osgi.service.http.HttpContext;
import org.osgi.service.http.NamespaceException;

public class ExtHttpServiceMock implements ExtHttpService {

	private final Map<Servlet, String> servletAlias = new HashMap<Servlet, String>();

	private final Map<String, String> resourceAlias = new HashMap<String, String>();

	private final Map<Filter, String> filterAlias = new HashMap<Filter, String>();

	private int unregisterServletCallCounter;

	private int unregisterFilterCallCounter;

	private int unregisterResourceCallCounter;

	private int registerServletCallCounter;

	private int registerFilterCallCounter;

	private int registerResourceCallCounter;

	public ExtHttpServiceMock() {
	}

	@Override
	public void registerServlet(final String alias, final Servlet servlet, @SuppressWarnings("rawtypes") final Dictionary initparams, final HttpContext context) throws ServletException,
			NamespaceException {
		servletAlias.put(servlet, alias);
		registerServletCallCounter++;
	}

	@Override
	public void registerResources(final String alias, final String name, final HttpContext context) throws NamespaceException {
		resourceAlias.put(alias, name);
		registerResourceCallCounter++;
	}

	@Override
	public void registerFilter(final Filter filter, final String pattern, @SuppressWarnings("rawtypes") final Dictionary initParams, final int ranking, final HttpContext context)
			throws ServletException {
		filterAlias.put(filter, pattern);
		registerFilterCallCounter++;
	}

	@Override
	public void unregister(final String alias) {
		resourceAlias.remove(alias);
		unregisterResourceCallCounter++;
	}

	@Override
	public HttpContext createDefaultHttpContext() {
		return null;
	}

	@Override
	public void unregisterFilter(final Filter filter) {
		servletAlias.remove(filter);
		unregisterFilterCallCounter++;
	}

	@Override
	public void unregisterServlet(final Servlet servlet) {
		servletAlias.remove(servlet);
		unregisterServletCallCounter++;
	}

	public boolean hasServlet(final Servlet servlet) {
		return servletAlias.containsKey(servlet);
	}

	public boolean hasFilter(final Filter filter) {
		return filterAlias.containsKey(filter);
	}

	public int getUnregisterServletCallCounter() {
		return unregisterServletCallCounter;
	}

	public int getUnregisterFilterCallCounter() {
		return unregisterFilterCallCounter;
	}

	public int getUnregisterResourceCallCounter() {
		return unregisterResourceCallCounter;
	}

	public int getRegisterServletCallCounter() {
		return registerServletCallCounter;
	}

	public int getRegisterFilterCallCounter() {
		return registerFilterCallCounter;
	}

	public int getRegisterResourceCallCounter() {
		return registerResourceCallCounter;
	}

	public boolean hasServletPath(final String path) {
		return servletAlias.containsValue(path);
	}
}
