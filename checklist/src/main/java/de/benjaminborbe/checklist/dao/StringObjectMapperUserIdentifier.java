package de.benjaminborbe.checklist.dao;

import de.benjaminborbe.authentication.api.UserIdentifier;
import de.benjaminborbe.tools.mapper.stringobject.StringObjectMapperBase;

public class StringObjectMapperUserIdentifier<B> extends StringObjectMapperBase<B, UserIdentifier> {

	public StringObjectMapperUserIdentifier(final String name) {
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
