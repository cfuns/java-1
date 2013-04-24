package de.benjaminborbe.crawler.api;

import java.net.URL;

public class CrawlerInstructionBuilder implements CrawlerInstruction {

	private final URL url;

	private final int timeout;

	public CrawlerInstructionBuilder(final URL url, final int timeout) {
		this.url = url;
		this.timeout = timeout;
	}

	@Override
	public URL getUrl() {
		return url;
	}

	@Override
	public int getTimeout() {
		return timeout;
	}

}
