package de.benjaminborbe.authentication.session;

import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.authentication.api.UserIdentifier;
import de.benjaminborbe.storage.tools.Entity;

public class SessionBean implements Entity<SessionIdentifier> {

	private static final long serialVersionUID = -3922883715303844030L;

	private SessionIdentifier id;

	private UserIdentifier currentUser;

	@Override
	public SessionIdentifier getId() {
		return id;
	}

	@Override
	public void setId(final SessionIdentifier id) {
		this.id = id;
	}

	public UserIdentifier getCurrentUser() {
		return currentUser;
	}

	public void setCurrentUser(final UserIdentifier currentUser) {
		this.currentUser = currentUser;
	}

}
