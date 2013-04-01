package de.benjaminborbe.authentication.core.dao;

import de.benjaminborbe.api.IdentifierBuilder;
import de.benjaminborbe.authentication.api.SessionIdentifier;

public class SessionIdentifierBuilder implements IdentifierBuilder<String, SessionIdentifier> {

	@Override
	public SessionIdentifier buildIdentifier(final String value) {
		return new SessionIdentifier(value);
	}

}
