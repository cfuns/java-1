package de.benjaminborbe.authentication.util;

import de.benjaminborbe.storage.tools.Entity;

public class UserBean implements Entity<String> {

	private static final long serialVersionUID = -3922883715303844030L;

	private String id;

	private String password;

	@Override
	public String getId() {
		return id;
	}

	@Override
	public void setId(final String id) {
		this.id = id;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(final String password) {
		this.password = password;
	}

}
