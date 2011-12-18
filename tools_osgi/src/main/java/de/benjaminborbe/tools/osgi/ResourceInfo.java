package de.benjaminborbe.tools.osgi;

import org.osgi.service.http.HttpContext;

public class ResourceInfo {

	private final String alias;

	private final String name;

	public ResourceInfo(final String alias, final String name) {
		this.alias = alias;
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public String getAlias() {
		return alias;
	}

	public HttpContext getContext() {
		return null;
	}
}
