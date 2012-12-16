package de.benjaminborbe.checklist.dao;

import de.benjaminborbe.authentication.api.UserIdentifier;
import de.benjaminborbe.tools.mapper.SingleMapBase;

public class SingleMapEntryIdentifier<B> extends SingleMapBase<B, UserIdentifier> {

	public SingleMapEntryIdentifier(final String name) {
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
