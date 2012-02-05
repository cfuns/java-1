package de.benjaminborbe.authorization.api;

public class RoleIdentifier {

	private final String id;

	public RoleIdentifier(final String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}
}
