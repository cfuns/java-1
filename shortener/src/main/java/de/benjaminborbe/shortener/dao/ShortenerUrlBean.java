package de.benjaminborbe.shortener.dao;

import java.util.Calendar;

import de.benjaminborbe.shortener.api.ShortenerUrlIdentifier;
import de.benjaminborbe.storage.tools.EntityBase;
import de.benjaminborbe.storage.tools.HasCreated;
import de.benjaminborbe.storage.tools.HasModified;

public class ShortenerUrlBean extends EntityBase<ShortenerUrlIdentifier> implements HasCreated, HasModified {

	private static final long serialVersionUID = -8803301003126328406L;

	private ShortenerUrlIdentifier id;

	private String url;

	private Calendar created;

	private Calendar modified;

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

	public String getUrl() {
		return url;
	}

	public void setUrl(final String url) {
		this.url = url;
	}

	@Override
	public ShortenerUrlIdentifier getId() {
		return id;
	}

	@Override
	public void setId(final ShortenerUrlIdentifier id) {
		this.id = id;
	}

}
