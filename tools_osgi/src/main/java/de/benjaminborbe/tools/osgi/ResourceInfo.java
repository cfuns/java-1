package de.benjaminborbe.tools.osgi;

import org.osgi.service.http.HttpContext;

public class ResourceInfo {

	private final String alias;

	private final String name;

	private final boolean slashResoure;

	public ResourceInfo(final String alias, final String name) {
		this(alias, name, false);
	}

	public ResourceInfo(final String alias, final String name, final boolean slashResoure) {
		this.alias = alias;
		this.name = name;
		this.slashResoure = slashResoure;
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

	public boolean isSlashResoure() {
		return slashResoure;
	}
}
