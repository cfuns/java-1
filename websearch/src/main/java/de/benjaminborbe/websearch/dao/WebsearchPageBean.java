package de.benjaminborbe.websearch.dao;

import java.util.Calendar;
import de.benjaminborbe.storage.tools.Entity;
import de.benjaminborbe.storage.tools.HasCreated;
import de.benjaminborbe.storage.tools.HasModified;
import de.benjaminborbe.websearch.api.WebsearchPage;
import de.benjaminborbe.websearch.api.WebsearchPageIdentifier;

public class WebsearchPageBean implements Entity<WebsearchPageIdentifier>, WebsearchPage, HasCreated, HasModified {

	private static final long serialVersionUID = -7689141287266279351L;

	private Calendar lastVisit;

	private WebsearchPageIdentifier id;

	private Calendar modified;

	private Calendar created;

	@Override
	public WebsearchPageIdentifier getId() {
		return id;
	}

	@Override
	public void setId(final WebsearchPageIdentifier id) {
		this.id = id;
	}

	@Override
	public Calendar getLastVisit() {
		return lastVisit;
	}

	public void setLastVisit(final Calendar lastVisit) {
		this.lastVisit = lastVisit;
	}

	@Override
	public String getUrl() {
		return id != null ? id.getId() : null;
	}

	public void setUrl(final String url) {
		id = new WebsearchPageIdentifier(url);
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

}
