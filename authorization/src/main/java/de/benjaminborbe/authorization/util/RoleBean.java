package de.benjaminborbe.authorization.util;

import de.benjaminborbe.storage.tools.Entity;

public class RoleBean implements Entity<String> {

	private static final long serialVersionUID = 5954692477523378479L;

	private String id;

	@Override
	public String getId() {
		return id;
	}

	@Override
	public void setId(final String id) {
		this.id = id;
	}

}
