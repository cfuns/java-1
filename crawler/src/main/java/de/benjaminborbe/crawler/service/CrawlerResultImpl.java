package de.benjaminborbe.crawler.service;

import java.net.URL;

import de.benjaminborbe.crawler.api.CrawlerResult;

public class CrawlerResultImpl implements CrawlerResult {

	private final URL url;

	private final String content;

	private final boolean available;

	public CrawlerResultImpl(final URL url, final String content, final boolean available) {
		this.url = url;
		this.content = content;
		this.available = available;
	}

	@Override
	public URL getUrl() {
		return url;
	}

	@Override
	public String getContent() {
		return content;
	}

	@Override
	public boolean isAvailable() {
		return available;
	}

}
