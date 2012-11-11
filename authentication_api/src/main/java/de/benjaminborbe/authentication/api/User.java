package de.benjaminborbe.authentication.api;

public interface User {

	UserIdentifier getId();

	String getEmail();

	String getFullname();

	boolean isSuperAdmin();
}
