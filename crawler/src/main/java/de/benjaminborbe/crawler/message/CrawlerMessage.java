package de.benjaminborbe.crawler.message;

import java.net.URL;

public class CrawlerMessage {

	private final URL url;

	private final int timeout;

	public CrawlerMessage(final URL url, final int timeout) {
		this.url = url;
		this.timeout = timeout;
	}

	public URL getUrl() {
		return url;
	}

	public int getTimeout() {
		return timeout;
	}

}
