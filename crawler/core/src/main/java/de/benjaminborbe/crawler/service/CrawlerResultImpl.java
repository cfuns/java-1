package de.benjaminborbe.crawler.service;

import de.benjaminborbe.crawler.api.CrawlerResult;
import de.benjaminborbe.httpdownloader.api.HttpContent;
import de.benjaminborbe.httpdownloader.api.HttpHeader;

import java.net.URL;

public class CrawlerResultImpl implements CrawlerResult {

	private final URL url;

	private final String content;

	private final String contentType;

	private final boolean available;

	public CrawlerResultImpl(final URL url, final String content, final String contentType, final boolean available) {
		this.url = url;
		this.content = content;
		this.contentType = contentType;
		this.available = available;
	}

	@Override
	public Integer getReturnCode() {
		return null;
	}

	@Override
	public URL getUrl() {
		return url;
	}

	@Override
	public Long getDuration() {
		return null;
	}

	@Override
	public HttpHeader getHeader() {
		return null;
	}

	@Override
	public HttpContent getContent() {
		return null;
	}
}
