package de.benjaminborbe.authorization.permission;

import de.benjaminborbe.api.IdentifierBuilder;
import de.benjaminborbe.authorization.api.PermissionIdentifier;

public class PermissionIdentifierBuilder implements IdentifierBuilder<String, PermissionIdentifier> {

	@Override
	public PermissionIdentifier buildIdentifier(final String value) {
		return new PermissionIdentifier(value);
	}

}
