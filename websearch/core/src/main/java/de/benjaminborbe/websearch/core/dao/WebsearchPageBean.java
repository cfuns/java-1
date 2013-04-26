package de.benjaminborbe.websearch.core.dao;

import de.benjaminborbe.httpdownloader.api.HttpHeader;
import de.benjaminborbe.storage.tools.EntityBase;
import de.benjaminborbe.storage.tools.HasCreated;
import de.benjaminborbe.storage.tools.HasModified;
import de.benjaminborbe.websearch.api.WebsearchPage;
import de.benjaminborbe.websearch.api.WebsearchPageIdentifier;

import java.net.URL;
import java.util.Calendar;

public class WebsearchPageBean extends EntityBase<WebsearchPageIdentifier> implements WebsearchPage, HasCreated, HasModified {

	private static final long serialVersionUID = -7689141287266279351L;

	private WebsearchPageIdentifier id;

	private URL url;

	public URL getUrl() {
		return url;
	}

	public void setUrl(final URL url) {
		this.url = url;
	}

	private HttpHeader header;

	public byte[] getContent() {
		return content;
	}

	public void setContent(final byte[] content) {
		this.content = content;
	}

	public HttpHeader getHeader() {
		return header;
	}

	public void setHeader(final HttpHeader header) {
		this.header = header;
	}

	private Calendar lastVisit;

	private Calendar modified;

	private Calendar created;

	private byte[] content;

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

}
