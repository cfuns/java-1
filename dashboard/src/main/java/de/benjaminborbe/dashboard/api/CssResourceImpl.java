package de.benjaminborbe.dashboard.api;


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
