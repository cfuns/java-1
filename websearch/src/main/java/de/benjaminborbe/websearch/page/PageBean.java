package de.benjaminborbe.websearch.page;

import java.net.URL;
import java.util.Date;

import de.benjaminborbe.storage.tools.Entity;
import de.benjaminborbe.websearch.api.Page;
import de.benjaminborbe.websearch.api.PageIdentifier;

public class PageBean implements Entity<PageIdentifier>, Page {

	private static final long serialVersionUID = -7689141287266279351L;

	private Date lastVisit;

	private PageIdentifier id;

	@Override
	public PageIdentifier getId() {
		return id;
	}

	@Override
	public void setId(final PageIdentifier id) {
		this.id = id;
	}

	@Override
	public Date getLastVisit() {
		return lastVisit;
	}

	public void setLastVisit(final Date lastVisit) {
		this.lastVisit = lastVisit;
	}

	@Override
	public URL getUrl() {
		return id != null ? id.getUrl() : null;
	}

	public void setUrl(final URL url) {
		id = new PageIdentifier(url);
	}

}
