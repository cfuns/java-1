package de.benjaminborbe.websearch.page;

import java.net.URL;
import java.util.Date;

import de.benjaminborbe.tools.dao.Entity;

public class PageBean implements Entity<URL> {

	private static final long serialVersionUID = -7689141287266279351L;

	private URL id;

	private Date lastVisit;

	public Date getLastVisit() {
		return lastVisit;
	}

	public void setLastVisit(final Date lastVisit) {
		this.lastVisit = lastVisit;
	}

	@Override
	public URL getId() {
		return id;
	}

	@Override
	public void setId(final URL id) {
		this.id = id;
	}
}
