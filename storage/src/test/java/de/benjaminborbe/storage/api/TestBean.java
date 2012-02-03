package de.benjaminborbe.storage.api;

import de.benjaminborbe.tools.dao.Entity;

public class TestBean implements Entity<String> {

	private static final long serialVersionUID = -4849861781774372129L;

	private String id;

	private String name;

	@Override
	public String getId() {
		return id;
	}

	@Override
	public void setId(final String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}
}
