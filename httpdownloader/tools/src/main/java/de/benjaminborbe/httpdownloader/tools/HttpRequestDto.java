package de.benjaminborbe.httpdownloader.tools;

import de.benjaminborbe.httpdownloader.api.HttpHeader;
import de.benjaminborbe.httpdownloader.api.HttpRequest;
import org.apache.commons.lang.builder.ToStringBuilder;

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

	@Override
	public boolean equals(final Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		final HttpRequestDto that = (HttpRequestDto) o;

		if (timeout != that.timeout) return false;
		if (header != null ? !header.equals(that.header) : that.header != null) return false;
		if (url != null ? !url.equals(that.url) : that.url != null) return false;

		return true;
	}

	@Override
	public int hashCode() {
		int result = timeout;
		result = 31 * result + (url != null ? url.hashCode() : 0);
		result = 31 * result + (header != null ? header.hashCode() : 0);
		return result;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this)
			.append("url", url)
			.append("timeout", timeout)
			.append("header", header)
			.toString();
	}
}
