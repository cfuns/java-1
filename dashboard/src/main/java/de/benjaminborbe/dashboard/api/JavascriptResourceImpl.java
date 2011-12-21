package de.benjaminborbe.dashboard.api;


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
