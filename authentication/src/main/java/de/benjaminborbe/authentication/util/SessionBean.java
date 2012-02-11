package de.benjaminborbe.authentication.util;

import de.benjaminborbe.storage.tools.Entity;

public class SessionBean implements Entity<String> {

	private static final long serialVersionUID = -3922883715303844030L;

	private String id;

	private String currentUser;

	@Override
	public String getId() {
		return id;
	}

	@Override
	public void setId(final String id) {
		this.id = id;
	}

	public String getCurrentUser() {
		return currentUser;
	}

	public void setCurrentUser(final String currentUser) {
		this.currentUser = currentUser;
	}

}
