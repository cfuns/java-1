package de.benjaminborbe.storage.api;

import de.benjaminborbe.tools.dao.EntityLong;

public class TestBean implements EntityLong {

	private static final long serialVersionUID = -4849861781774372129L;

	private Long id;

	private String name;

	@Override
	public Long getId() {
		return id;
	}

	@Override
	public void setId(final Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}
}
