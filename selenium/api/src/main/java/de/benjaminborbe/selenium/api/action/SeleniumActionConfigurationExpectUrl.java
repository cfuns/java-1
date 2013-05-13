package de.benjaminborbe.selenium.api.action;

import java.net.URL;

public class SeleniumActionConfigurationExpectUrl implements SeleniumActionConfiguration {

	private final String message;

	private final URL url;

	public SeleniumActionConfigurationExpectUrl(final String message, final URL url) {
		this.message = message;
		this.url = url;
	}

	public String getMessage() {
		return message;
	}

	public URL getUrl() {
		return url;
	}
}
