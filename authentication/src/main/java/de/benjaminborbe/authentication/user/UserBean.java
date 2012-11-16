package de.benjaminborbe.authentication.user;

import java.util.Calendar;

import de.benjaminborbe.authentication.api.User;
import de.benjaminborbe.authentication.api.UserIdentifier;
import de.benjaminborbe.storage.tools.Entity;
import de.benjaminborbe.storage.tools.HasCreated;
import de.benjaminborbe.storage.tools.HasModified;

public class UserBean implements Entity<UserIdentifier>, User, HasCreated, HasModified {

	private static final long serialVersionUID = -3922883715303844030L;

	private UserIdentifier id;

	private byte[] password;

	private byte[] passwordSalt;

	private String email;

	private String fullname;

	private Boolean superAdmin;

	private Calendar modified;

	private Calendar created;

	@Override
	public UserIdentifier getId() {
		return id;
	}

	@Override
	public void setId(final UserIdentifier id) {
		this.id = id;
	}

	public byte[] getPassword() {
		return password;
	}

	public void setPassword(final byte[] password) {
		this.password = password;
	}

	@Override
	public String getEmail() {
		return email;
	}

	public void setEmail(final String email) {
		this.email = email;
	}

	@Override
	public String getFullname() {
		return fullname;
	}

	public void setFullname(final String fullname) {
		this.fullname = fullname;
	}

	public byte[] getPasswordSalt() {
		return passwordSalt;
	}

	public void setPasswordSalt(final byte[] passwordSalt) {
		this.passwordSalt = passwordSalt;
	}

	@Override
	public Boolean getSuperAdmin() {
		return superAdmin;
	}

	public void setSuperAdmin(final Boolean admin) {
		this.superAdmin = admin;
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
