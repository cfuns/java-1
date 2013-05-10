package de.benjaminborbe.tools.osgi;

import org.osgi.service.http.HttpContext;

import javax.servlet.http.HttpServlet;
import java.util.Properties;

public class ServletInfo {

	private final HttpServlet servlet;

	private final String alias;

	private final boolean slashServlet;

	public ServletInfo(final HttpServlet servlet, final String alias) {
		this(servlet, alias, false);
	}

	public ServletInfo(final HttpServlet servlet, final String alias, final boolean slashServlet) {
		this.servlet = servlet;
		this.alias = alias;
		this.slashServlet = slashServlet;
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

	public boolean isSlashServlet() {
		return slashServlet;
	}
}
