package de.benjaminborbe.authentication.api;

public class UserIdentifier {

	private final String id;

	public UserIdentifier(final String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}
}
