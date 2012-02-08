package de.benjaminborbe.crawler.api;

import java.net.URL;

public class CrawlerInstructionBuilder implements CrawlerInstruction {

	private final URL url;

	public CrawlerInstructionBuilder(final URL url) {
		this.url = url;
	}

	@Override
	public URL getUrl() {
		return url;
	}

}
