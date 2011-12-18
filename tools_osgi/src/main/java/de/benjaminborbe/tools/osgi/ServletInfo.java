package de.benjaminborbe.tools.osgi;

import java.util.Properties;

import javax.servlet.http.HttpServlet;

import org.osgi.service.http.HttpContext;

public class ServletInfo {

	private final HttpServlet servlet;

	private final String alias;

	public ServletInfo(final HttpServlet servlet, final String alias) {
		this.servlet = servlet;
		this.alias = alias;
	}

	public HttpServlet getServlet() {
		return servlet;
	}

	public HttpContext getContext() {
		return null;
	}

	public Properties getInitParams() {
		return null;
	}

	public String getAlias() {
		return alias;
	}
}
