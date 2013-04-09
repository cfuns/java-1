package de.benjaminborbe.crawler.api;

public class CrawlerInstructionBuilder implements CrawlerInstruction {

	private final String url;

	private final int timeout;

	public CrawlerInstructionBuilder(final String url, final int timeout) {
		this.url = url;
		this.timeout = timeout;
	}

	@Override
	public String getUrl() {
		return url;
	}

	@Override
	public int getTimeout() {
		return timeout;
	}

}
