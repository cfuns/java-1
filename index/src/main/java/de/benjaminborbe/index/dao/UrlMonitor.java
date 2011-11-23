package de.benjaminborbe.index.dao;

import java.net.URL;

import de.benjaminborbe.tools.dao.Entity;

public class UrlMonitor implements Entity {

	private static final long serialVersionUID = -7570634838176963888L;

	private Long id;

	private URL url;

	private int timeout;

	private String titleMatch;

	private String bodyMatch;

	@Override
	public Long getId() {
		return id;
	}

	@Override
	public void setId(final Long id) {
		this.id = id;
	}

	public URL getUrl() {
		return url;
	}

	public void setUrl(final URL url) {
		this.url = url;
	}

	public int getTimeout() {
		return timeout;
	}

	public void setTimeout(final int timeout) {
		this.timeout = timeout;
	}

	public String getTitleMatch() {
		return titleMatch;
	}

	public void setTitleMatch(final String titleMatch) {
		this.titleMatch = titleMatch;
	}

	public String getBodyMatch() {
		return bodyMatch;
	}

	public void setBodyMatch(final String bodyMatch) {
		this.bodyMatch = bodyMatch;
	}
}
