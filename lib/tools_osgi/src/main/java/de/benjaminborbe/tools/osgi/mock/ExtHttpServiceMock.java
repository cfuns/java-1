package de.benjaminborbe.tools.osgi.mock;

import javax.inject.Inject;
import de.benjaminborbe.tools.url.UrlUtil;
import org.apache.felix.http.api.ExtHttpService;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleException;
import org.osgi.framework.ServiceReference;
import org.osgi.service.http.HttpContext;
import org.osgi.service.http.NamespaceException;

import javax.servlet.Filter;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Collection;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

public class ExtHttpServiceMock implements ExtHttpService, Bundle {

	private final Map<Servlet, String> servletAlias = new HashMap<>();

	private final Map<String, String> resourceAlias = new HashMap<>();

	private final Map<Filter, String> filterAlias = new HashMap<>();

	private int unregisterServletCallCounter;

	private int unregisterFilterCallCounter;

	private int unregisterResourceCallCounter;

	private int registerServletCallCounter;

	private int registerFilterCallCounter;

	private int registerResourceCallCounter;

	private final UrlUtil urlUtil;

	@Inject
	public ExtHttpServiceMock(final UrlUtil urlUtil) {
		this.urlUtil = urlUtil;
	}

	public Collection<Filter> getFilters() {
		return filterAlias.keySet();
	}

	public Collection<Servlet> getServlets() {
		return servletAlias.keySet();
	}

	@Override
	public void registerServlet(final String alias, final Servlet servlet, @SuppressWarnings("rawtypes") final Dictionary initparams, final HttpContext context)
		throws ServletException, NamespaceException {
		servletAlias.put(servlet, urlUtil.removeTailingSlash(alias));
		registerServletCallCounter++;
	}

	@Override
	public void registerResources(final String alias, final String name, final HttpContext context) throws NamespaceException {
		resourceAlias.put(urlUtil.removeTailingSlash(alias), name);
		registerResourceCallCounter++;
	}

	@Override
	public void registerFilter(final Filter filter, final String pattern, @SuppressWarnings("rawtypes") final Dictionary initParams, final int ranking, final HttpContext context)
		throws ServletException {
		filterAlias.put(filter, pattern);
		registerFilterCallCounter++;
	}

	@Override
	public void unregister(final String path) {
		resourceAlias.remove(urlUtil.removeTailingSlash(path));
		unregisterResourceCallCounter++;
	}

	@Override
	public HttpContext createDefaultHttpContext() {
		return null;
	}

	@Override
	public void unregisterFilter(final Filter filter) {
		filterAlias.remove(filter);
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

	public boolean hasResource(final String path) {
		return resourceAlias.containsKey(urlUtil.removeTailingSlash(path));
	}

	public boolean hasFilter(final Filter filter) {
		return filterAlias.containsKey(filter);
	}

	public boolean hasFilterPath(final String path) {
		return filterAlias.containsValue(urlUtil.removeTailingSlash(path));
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
		return servletAlias.containsValue(urlUtil.removeTailingSlash(path));
	}

	@Override
	public int getState() {

		return 0;
	}

	@Override
	public void start() throws BundleException {

	}

	@Override
	public void stop() throws BundleException {

	}

	@Override
	public void update() throws BundleException {

	}

	@Override
	public void update(final InputStream in) throws BundleException {

	}

	@Override
	public void uninstall() throws BundleException {

	}

	@SuppressWarnings("rawtypes")
	@Override
	public Dictionary getHeaders() {

		return null;
	}

	@Override
	public long getBundleId() {

		return 0;
	}

	@Override
	public String getLocation() {

		return null;
	}

	@Override
	public ServiceReference[] getRegisteredServices() {

		return null;
	}

	@Override
	public ServiceReference[] getServicesInUse() {

		return null;
	}

	@Override
	public boolean hasPermission(final Object permission) {

		return false;
	}

	@Override
	public URL getResource(final String name) {

		return null;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Dictionary getHeaders(final String locale) {

		return null;
	}

	@Override
	public String getSymbolicName() {

		return null;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Class loadClass(final String name) throws ClassNotFoundException {

		return null;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Enumeration getResources(final String name) throws IOException {

		return null;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Enumeration getEntryPaths(final String path) {

		return null;
	}

	@Override
	public URL getEntry(final String name) {

		return null;
	}

	@Override
	public long getLastModified() {

		return 0;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Enumeration findEntries(final String path, final String filePattern, final boolean recurse) {

		return null;
	}

}
