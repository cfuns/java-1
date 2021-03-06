package de.benjaminborbe.confluence.dao;

import de.benjaminborbe.authentication.api.UserIdentifier;
import de.benjaminborbe.confluence.api.ConfluenceInstanceIdentifier;
import de.benjaminborbe.confluence.api.ConfluencePage;
import de.benjaminborbe.confluence.api.ConfluencePageIdentifier;
import de.benjaminborbe.storage.tools.EntityBase;
import de.benjaminborbe.storage.tools.HasCreated;
import de.benjaminborbe.storage.tools.HasModified;

import java.net.URL;
import java.util.Calendar;

public class ConfluencePageBean extends EntityBase<ConfluencePageIdentifier> implements ConfluencePage, HasCreated, HasModified {

	private static final long serialVersionUID = -8803301003126328406L;

	private ConfluencePageIdentifier id;

	private Calendar lastVisit;

	private Calendar lastModified;

	private Calendar modified;

	private Calendar created;

	private ConfluenceInstanceIdentifier instanceId;

	private String pageId;

	private UserIdentifier owner;

	private URL url;

	@Override
	public Calendar getCreated() {
		return created;
	}

	@Override
	public void setCreated(final Calendar created) {
		this.created = created;
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
	public Calendar getLastVisit() {
		return lastVisit;
	}

	public void setLastVisit(final Calendar lastVisit) {
		this.lastVisit = lastVisit;
	}

	@Override
	public ConfluenceInstanceIdentifier getInstanceId() {
		return instanceId;
	}

	public void setInstanceId(final ConfluenceInstanceIdentifier instanceId) {
		this.instanceId = instanceId;
	}

	@Override
	public String getPageId() {
		return pageId;
	}

	public void setPageId(final String pageId) {
		this.pageId = pageId;
	}

	public UserIdentifier getOwner() {
		return owner;
	}

	public void setOwner(final UserIdentifier owner) {
		this.owner = owner;
	}

	public URL getUrl() {
		return url;
	}

	public void setUrl(final URL url) {
		this.url = url;
	}

	@Override
	public ConfluencePageIdentifier getId() {
		return id;
	}

	@Override
	public void setId(final ConfluencePageIdentifier id) {
		this.id = id;
	}

	public Calendar getLastModified() {
		return lastModified;
	}

	public void setLastModified(final Calendar lastModified) {
		this.lastModified = lastModified;
	}

}
