package de.benjaminborbe.crawler.api;

import java.net.URL;

public class CrawlerInstructionBuilder implements CrawlerInstruction {

	private final URL url;

	private final Long depth;

	private final Integer timeout;

	public CrawlerInstructionBuilder(final URL url, final Long depth, final Integer timeout) {
		this.url = url;
		this.depth = depth;
		this.timeout = timeout;
	}

	@Override
	public URL getUrl() {
		return url;
	}

	@Override
	public Long getDepth() {
		return depth;
	}

	@Override
	public Integer getTimeout() {
		return timeout;
	}
}
