package de.benjaminborbe.authentication.session;

import java.util.Calendar;

import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.authentication.api.UserIdentifier;
import de.benjaminborbe.storage.tools.Entity;
import de.benjaminborbe.storage.tools.HasCreated;
import de.benjaminborbe.storage.tools.HasModified;

public class SessionBean implements Entity<SessionIdentifier>, HasCreated, HasModified {

	private static final long serialVersionUID = -3922883715303844030L;

	private SessionIdentifier id;

	private UserIdentifier currentUser;

	private Calendar modified;

	private Calendar created;

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

}
