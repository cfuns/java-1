package de.benjaminborbe.websearch.page;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;

import de.benjaminborbe.storage.tools.Entity;
import de.benjaminborbe.websearch.api.Page;

public class PageBean implements Entity<String>, Page {

	private static final long serialVersionUID = -7689141287266279351L;

	private URL url;

	private Date lastVisit;

	@Override
	public Date getLastVisit() {
		return lastVisit;
	}

	public void setLastVisit(final Date lastVisit) {
		this.lastVisit = lastVisit;
	}

	@Override
	public String getId() {
		return url != null ? url.toExternalForm() : null;
	}

	@Override
	public void setId(final String id) {
		try {
			this.url = new URL(id);
		}
		catch (final MalformedURLException e) {
		}
	}

	@Override
	public URL getUrl() {
		return url;
	}

	public void setUrl(final URL url) {
		this.url = url;
	}

}
