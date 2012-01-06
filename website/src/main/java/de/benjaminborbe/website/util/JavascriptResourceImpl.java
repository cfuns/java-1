package de.benjaminborbe.website.util;

import de.benjaminborbe.html.api.JavascriptResource;

public class JavascriptResourceImpl implements JavascriptResource {

	private final String path;

	public JavascriptResourceImpl(final String path) {
		this.path = path;
	}

	@Override
	public String getUrl() {
		return path;
	}

}
