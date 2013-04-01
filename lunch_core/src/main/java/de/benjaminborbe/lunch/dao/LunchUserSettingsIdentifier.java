package de.benjaminborbe.lunch.dao;

import de.benjaminborbe.api.IdentifierBase;

public class LunchUserSettingsIdentifier extends IdentifierBase<String> {

	public LunchUserSettingsIdentifier(final long id) {
		this(String.valueOf(id));
	}

	public LunchUserSettingsIdentifier(final String id) {
		super(id);
	}

}
