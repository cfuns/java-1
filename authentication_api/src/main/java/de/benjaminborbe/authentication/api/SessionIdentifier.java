package de.benjaminborbe.authentication.api;

import javax.servlet.http.HttpServletRequest;

public class SessionIdentifier {

	private final String id;

	public SessionIdentifier(final HttpServletRequest request) {
		this.id = request.getSession().getId();
	}

	public SessionIdentifier(final String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}

}
