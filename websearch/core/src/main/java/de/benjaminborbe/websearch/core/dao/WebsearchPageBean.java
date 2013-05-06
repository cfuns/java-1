package de.benjaminborbe.websearch.core.dao;

import de.benjaminborbe.crawler.api.CrawlerNotifierResult;
import de.benjaminborbe.httpdownloader.api.HttpContent;
import de.benjaminborbe.httpdownloader.api.HttpHeader;
import de.benjaminborbe.storage.tools.EntityBase;
import de.benjaminborbe.storage.tools.HasCreated;
import de.benjaminborbe.storage.tools.HasModified;
import de.benjaminborbe.websearch.api.WebsearchPage;
import de.benjaminborbe.websearch.api.WebsearchPageIdentifier;

import java.net.URL;
import java.util.Calendar;

public class WebsearchPageBean extends EntityBase<WebsearchPageIdentifier> implements WebsearchPage, CrawlerNotifierResult, HasCreated, HasModified {

	private static final long serialVersionUID = -7689141287266279351L;

	private WebsearchPageIdentifier id;

	private URL url;

	private Calendar lastVisit;

	private Calendar modified;

	private Calendar created;

	private HttpHeader header;

	private Integer returnCode;

	private Long duration;

	private HttpContent content;

	private Long depth;

	private Integer timeout;

	public Integer getTimeout() {
		return timeout;
	}

	public void setTimeout(final Integer timeout) {
		this.timeout = timeout;
	}

	public Long getDepth() {
		return depth;
	}

	public void setDepth(final Long depth) {
		this.depth = depth;
	}

	public Long getDuration() {
		return duration;
	}

	public void setDuration(final Long duration) {
		this.duration = duration;
	}

	@Override
	public URL getUrl() {
		return url;
	}

	public void setUrl(final URL url) {
		this.url = url;
	}

	@Override
	public HttpHeader getHeader() {
		return header;
	}

	@Override
	public HttpContent getContent() {
		return content;
	}

	public void setHeader(final HttpHeader header) {
		this.header = header;
	}

	@Override
	public Calendar getLastVisit() {
		return lastVisit;
	}

	public void setLastVisit(final Calendar lastVisit) {
		this.lastVisit = lastVisit;
	}

	@Override
	public Calendar getModified() {
		return modified;
	}

	@Override
	public void setModified(final Calendar modified) {
		this.modified = modified;
	}

	@Override
	public Calendar getCreated() {
		return created;
	}

	@Override
	public void setCreated(final Calendar created) {
		this.created = created;
	}

	@Override
	public WebsearchPageIdentifier getId() {
		return id;
	}

	@Override
	public void setId(final WebsearchPageIdentifier id) {
		this.id = id;
	}

	@Override
	public Integer getReturnCode() {
		return returnCode;
	}

	public void setReturnCode(final Integer returnCode) {
		this.returnCode = returnCode;
	}

	public void setContent(final HttpContent content) {
		this.content = content;
	}
}
