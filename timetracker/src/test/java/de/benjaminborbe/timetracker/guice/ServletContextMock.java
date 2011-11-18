package de.benjaminborbe.timetracker.guice;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Enumeration;
import java.util.Set;

import javax.servlet.RequestDispatcher;
import javax.servlet.Servlet;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;

public class ServletContextMock implements ServletContext {

	public Object getAttribute(final String arg0) {
		return null;
	}

	@SuppressWarnings("rawtypes")
	public Enumeration getAttributeNames() {
		return null;
	}

	public ServletContext getContext(final String arg0) {
		return null;
	}

	public String getContextPath() {
		return null;
	}

	public String getInitParameter(final String arg0) {
		return null;
	}

	@SuppressWarnings("rawtypes")
	public Enumeration getInitParameterNames() {
		return null;
	}

	public int getMajorVersion() {
		return 0;
	}

	public String getMimeType(final String arg0) {
		return null;
	}

	public int getMinorVersion() {
		return 0;
	}

	public RequestDispatcher getNamedDispatcher(final String arg0) {
		return null;
	}

	public String getRealPath(final String arg0) {
		return null;
	}

	public RequestDispatcher getRequestDispatcher(final String arg0) {
		return null;
	}

	public URL getResource(final String path) throws MalformedURLException {
		return getClass().getClassLoader().getResource(path);
	}

	public InputStream getResourceAsStream(final String arg0) {
		return null;
	}

	@SuppressWarnings("rawtypes")
	public Set getResourcePaths(final String arg0) {
		return null;
	}

	public String getServerInfo() {
		return null;
	}

	public Servlet getServlet(final String arg0) throws ServletException {
		return null;
	}

	public String getServletContextName() {
		return null;
	}

	@SuppressWarnings("rawtypes")
	public Enumeration getServletNames() {
		return null;
	}

	@SuppressWarnings("rawtypes")
	public Enumeration getServlets() {
		return null;
	}

	public void log(final String arg0) {

	}

	public void log(final Exception arg0, final String arg1) {
	}

	public void log(final String arg0, final Throwable arg1) {
	}

	public void removeAttribute(final String arg0) {
	}

	public void setAttribute(final String arg0, final Object arg1) {
	}

}
