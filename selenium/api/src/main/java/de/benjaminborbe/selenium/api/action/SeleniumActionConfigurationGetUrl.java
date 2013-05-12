package de.benjaminborbe.selenium.api.action;

import java.net.URL;

public class SeleniumActionConfigurationGetUrl implements SeleniumActionConfiguration {

	private String message;

	private URL url;

	public SeleniumActionConfigurationGetUrl(final String message, final URL url) {
		this.message = message;
		this.url = url;
	}

	public URL getUrl() {
		return url;
	}

	@Override
	public String getMessage() {
		return message;
	}
}
