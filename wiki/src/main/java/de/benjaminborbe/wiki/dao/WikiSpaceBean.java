package de.benjaminborbe.wiki.dao;

import de.benjaminborbe.storage.tools.Entity;
import de.benjaminborbe.wiki.api.WikiSpace;
import de.benjaminborbe.wiki.api.WikiSpaceIdentifier;

public class WikiSpaceBean implements Entity<WikiSpaceIdentifier>, WikiSpace {

	private static final long serialVersionUID = 6058606350883201939L;

	private WikiSpaceIdentifier id;

	private String name;

	@Override
	public WikiSpaceIdentifier getId() {
		return id;
	}

	@Override
	public void setId(final WikiSpaceIdentifier id) {
		this.id = id;
	}

	@Override
	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}
}
