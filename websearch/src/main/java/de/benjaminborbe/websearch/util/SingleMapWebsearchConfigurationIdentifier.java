package de.benjaminborbe.websearch.util;

import de.benjaminborbe.authentication.api.UserIdentifier;
import de.benjaminborbe.tools.mapper.SingleMapBase;

public class SingleMapWebsearchConfigurationIdentifier<B> extends SingleMapBase<B, UserIdentifier> {

	public SingleMapWebsearchConfigurationIdentifier(final String name) {
		super(name);
	}

	@Override
	public String toString(final UserIdentifier value) {
		return value != null ? value.getId() : null;
	}

	@Override
	public UserIdentifier fromString(final String value) {
		return value != null ? new UserIdentifier(value) : null;
	}

}
