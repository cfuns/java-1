package de.benjaminborbe.wiki.dao;

import de.benjaminborbe.storage.tools.EntityBase;
import de.benjaminborbe.storage.tools.HasCreated;
import de.benjaminborbe.storage.tools.HasModified;
import de.benjaminborbe.wiki.api.WikiSpace;
import de.benjaminborbe.wiki.api.WikiSpaceIdentifier;

import java.util.Calendar;

public class WikiSpaceBean extends EntityBase<WikiSpaceIdentifier> implements WikiSpace, HasCreated, HasModified {

	private static final long serialVersionUID = 6058606350883201939L;

	private WikiSpaceIdentifier id;

	private String name;

	private Calendar modified;

	private Calendar created;

	@Override
	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
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
	public WikiSpaceIdentifier getId() {
		return id;
	}

	@Override
	public void setId(final WikiSpaceIdentifier id) {
		this.id = id;
	}

}
