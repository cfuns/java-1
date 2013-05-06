package de.benjaminborbe.crawler.message;

import java.net.URL;

public class CrawlerMessage {

	private URL url;

	private long depth;

	private int timeout;

	public CrawlerMessage() {
	}

	public CrawlerMessage(final URL url, final long depth, final int timeout) {
		this.url = url;
		this.depth = depth;
		this.timeout = timeout;
	}

	public long getDepth() {
		return depth;
	}

	public void setDepth(final long depth) {
		this.depth = depth;
	}

	public URL getUrl() {
		return url;
	}

	public int getTimeout() {
		return timeout;
	}

	public void setUrl(final URL url) {
		this.url = url;
	}

	public void setTimeout(final int timeout) {
		this.timeout = timeout;
	}

}
