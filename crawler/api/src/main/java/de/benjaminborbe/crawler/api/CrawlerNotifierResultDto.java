package de.benjaminborbe.crawler.api;

import de.benjaminborbe.httpdownloader.api.HttpContent;
import de.benjaminborbe.httpdownloader.api.HttpHeader;
import de.benjaminborbe.httpdownloader.api.HttpResponse;

import java.net.URL;

public class CrawlerNotifierResultDto implements CrawlerNotifierResult {

	private HttpContent content;

	private HttpHeader header;

	private Long duration;

	private URL url;

	private Integer returnCode;

	private Long depth;

	private Integer timeout;

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

	public CrawlerNotifierResultDto() {
	}

	public CrawlerNotifierResultDto(HttpResponse httpResponse, final Long depth, Integer timeout) {
		this.depth = depth;
		this.timeout = timeout;
		this.content = httpResponse.getContent();
		this.header = httpResponse.getHeader();
		this.duration = httpResponse.getDuration();
		this.url = httpResponse.getUrl();
		this.returnCode = httpResponse.getReturnCode();
	}

	public void setContent(final HttpContent content) {
		this.content = content;
	}

	public void setDuration(final Long duration) {
		this.duration = duration;
	}

	public void setHeader(final HttpHeader header) {
		this.header = header;
	}

	public void setReturnCode(final Integer returnCode) {
		this.returnCode = returnCode;
	}

	public void setUrl(final URL url) {
		this.url = url;
	}

	@Override
	public Integer getReturnCode() {
		return returnCode;
	}

	@Override
	public URL getUrl() {
		return url;
	}

	@Override
	public Long getDuration() {
		return duration;
	}

	@Override
	public HttpHeader getHeader() {
		return header;
	}

	@Override
	public HttpContent getContent() {
		return content;
	}
}
