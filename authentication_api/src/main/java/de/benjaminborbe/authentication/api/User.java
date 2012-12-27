package de.benjaminborbe.authentication.api;

import java.util.TimeZone;

public interface User {

	UserIdentifier getId();

	String getEmail();

	Boolean getEmailVerified();

	String getFullname();

	Boolean getSuperAdmin();

	TimeZone getTimeZone();

}
