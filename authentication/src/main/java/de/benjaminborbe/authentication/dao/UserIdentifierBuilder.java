package de.benjaminborbe.authentication.dao;

import de.benjaminborbe.api.IdentifierBuilder;
import de.benjaminborbe.authentication.api.UserIdentifier;

public class UserIdentifierBuilder implements IdentifierBuilder<String, UserIdentifier> {

	@Override
	public UserIdentifier buildIdentifier(final String value) {
		return new UserIdentifier(value);
	}

}
