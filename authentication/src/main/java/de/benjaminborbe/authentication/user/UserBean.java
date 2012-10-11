package de.benjaminborbe.authentication.user;

import de.benjaminborbe.authentication.api.User;
import de.benjaminborbe.authentication.api.UserIdentifier;
import de.benjaminborbe.storage.tools.Entity;

public class UserBean implements Entity<UserIdentifier>, User {

	private static final long serialVersionUID = -3922883715303844030L;

	private UserIdentifier id;

	private String password;

	private String email;

	private String fullname;

	@Override
	public UserIdentifier getId() {
		return id;
	}

	@Override
	public void setId(final UserIdentifier id) {
		this.id = id;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(final String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(final String email) {
		this.email = email;
	}

	public String getFullname() {
		return fullname;
	}

	public void setFullname(final String fullname) {
		this.fullname = fullname;
	}

}
