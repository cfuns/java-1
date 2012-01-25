package de.benjaminborbe.crawler.service;

import de.benjaminborbe.crawler.api.CrawlerResult;

public class CrawlerResultImpl implements CrawlerResult {

	private final String url;

	private final String content;

	public CrawlerResultImpl(final String url, final String content) {
		this.url = url;
		this.content = content;
	}

	@Override
	public String getUrl() {
		return url;
	}

	@Override
	public String getContent() {
		return content;
	}

}
