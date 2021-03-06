package de.benjaminborbe.wow.core.dao;

import de.benjaminborbe.api.IdentifierBuilder;

public class WowAccountIdentifierBuilder implements IdentifierBuilder<String, WowAccountIdentifier> {

	@Override
	public WowAccountIdentifier buildIdentifier(final String value) {
		return new WowAccountIdentifier(value);
	}

}
