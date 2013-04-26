package de.benjaminborbe.httpdownloader.api;

import java.net.URL;

public class HttpResponseDto implements HttpResponse {

	private HttpContent content;

	private HttpHeader header;

	private Long duration;

	private URL url;

	private Integer returnCode;

	public void setReturnCode(final Integer returnCode) {
		this.returnCode = returnCode;
	}

	public HttpContent getContent() {
		return content;
	}

	public void setContent(final HttpContent content) {
		this.content = content;
	}

	public Long getDuration() {
		return duration;
	}

	public void setDuration(final Long duration) {
		this.duration = duration;
	}

	public HttpHeader getHeader() {
		return header;
	}

	public void setHeader(final HttpHeader header) {
		this.header = header;
	}

	@Override
	public Integer getReturnCode() {
		return returnCode;
	}

	public URL getUrl() {
		return url;
	}

	public void setUrl(final URL url) {
		this.url = url;
	}
}
