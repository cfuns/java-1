package de.benjaminborbe.authorization.role;

import de.benjaminborbe.api.IdentifierBuilder;
import de.benjaminborbe.authorization.api.RoleIdentifier;

public class RoleIdentifierBuilder implements IdentifierBuilder<String, RoleIdentifier> {

	@Override
	public RoleIdentifier buildIdentifier(final String value) {
		return new RoleIdentifier(value);
	}

}
