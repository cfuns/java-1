package de.benjaminborbe.lunch.dao;

import de.benjaminborbe.api.IdentifierBuilder;

public class LunchUserSettingsIdentifierBuilder implements IdentifierBuilder<String, LunchUserSettingsIdentifier> {

	@Override
	public LunchUserSettingsIdentifier buildIdentifier(final String value) {
		return new LunchUserSettingsIdentifier(value);
	}

}
