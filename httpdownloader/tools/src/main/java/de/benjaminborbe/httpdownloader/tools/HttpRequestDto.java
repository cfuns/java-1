package de.benjaminborbe.httpdownloader.tools;

import de.benjaminborbe.httpdownloader.api.HttpHeader;
import de.benjaminborbe.httpdownloader.api.HttpRequest;

import java.net.URL;

public class HttpRequestDto implements HttpRequest {

	private int timeout;

	private URL url;

	private HttpHeader header;

	public HttpRequestDto() {
	}

	public HttpRequestDto(final URL url) {
		this.timeout = timeout;
		this.url = url;
	}

	public HttpRequestDto(final URL url, final int timeout) {
		this.timeout = timeout;
		this.url = url;
	}

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
