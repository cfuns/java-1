package de.benjaminborbe.authentication.api;

import java.util.TimeZone;

public class UserDto implements User {

	private UserIdentifier id;

	private String email;

	private Boolean emailVerified;

	private String fullname;

	private Boolean superAdmin;

	private TimeZone timeZone;

	@Override
	public UserIdentifier getId() {
		return id;
	}

	public void setId(final UserIdentifier id) {
		this.id = id;
	}

	@Override
	public String getEmail() {
		return email;
	}

	public void setEmail(final String email) {
		this.email = email;
	}

	@Override
	public Boolean getEmailVerified() {
		return emailVerified;
	}

	public void setEmailVerified(final Boolean emailVerified) {
		this.emailVerified = emailVerified;
	}

	@Override
	public String getFullname() {
		return fullname;
	}

	public void setFullname(final String fullname) {
		this.fullname = fullname;
	}

	@Override
	public Boolean getSuperAdmin() {
		return superAdmin;
	}

	public void setSuperAdmin(final Boolean superAdmin) {
		this.superAdmin = superAdmin;
	}

	@Override
	public TimeZone getTimeZone() {
		return timeZone;
	}

	public void setTimeZone(final TimeZone timeZone) {
		this.timeZone = timeZone;
	}

}
