package de.benjaminborbe.httpdownloader.api;

import java.net.URL;

public class HttpRequestDto implements HttpRequest {

	private int timeout;

	private URL url;

	private HttpHeader header;

	public int getTimeout() {
		return timeout;
	}

	public void setTimeout(final int timeout) {
		this.timeout = timeout;
	}

	public URL getUrl() {
		return url;
	}

	public void setUrl(final URL url) {
		this.url = url;
	}

	public HttpHeader getHeader() {

		return header;
	}

	public void setHeader(final HttpHeader header) {
		this.header = header;
	}
}
