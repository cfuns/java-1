package de.benjaminborbe.crawler.service;

import java.net.URL;

import de.benjaminborbe.crawler.api.CrawlerResult;

public class CrawlerResultImpl implements CrawlerResult {

	private final URL url;

	private final String content;

	public CrawlerResultImpl(final URL url, final String content) {
		this.url = url;
		this.content = content;
	}

	@Override
	public URL getUrl() {
		return url;
	}

	@Override
	public String getContent() {
		return content;
	}

}
