package de.benjaminborbe.crawler.message;

import java.net.URL;

public class CrawlerMessage {

	private URL url;

	private Long depth;

	private Integer timeout;

	public CrawlerMessage() {
	}

	public CrawlerMessage(final URL url, final Long depth, final Integer timeout) {
		this.url = url;
		this.depth = depth;
		this.timeout = timeout;
	}

	public Long getDepth() {
		return depth;
	}

	public void setDepth(final Long depth) {
		this.depth = depth;
	}

	public Integer getTimeout() {
		return timeout;
	}

	public void setTimeout(final Integer timeout) {
		this.timeout = timeout;
	}

	public URL getUrl() {
		return url;
	}

	public void setUrl(final URL url) {
		this.url = url;
	}
}
