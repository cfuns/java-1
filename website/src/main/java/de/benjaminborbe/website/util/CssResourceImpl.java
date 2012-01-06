package de.benjaminborbe.website.util;

import de.benjaminborbe.html.api.CssResource;

public class CssResourceImpl implements CssResource {

	private final String path;

	public CssResourceImpl(final String path) {
		this.path = path;
	}

	@Override
	public String getUrl() {
		return path;
	}

}
