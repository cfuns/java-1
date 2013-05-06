package de.benjaminborbe.crawler.api;

import java.net.URL;

public class CrawlerInstructionBuilder implements CrawlerInstruction {

	private final URL url;

	private final long depth;

	private final int timeout;

	public CrawlerInstructionBuilder(final URL url, final long depth, final int timeout) {
		this.url = url;
		this.depth = depth;
		this.timeout = timeout;
	}

	public long getDepth() {
		return depth;
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
