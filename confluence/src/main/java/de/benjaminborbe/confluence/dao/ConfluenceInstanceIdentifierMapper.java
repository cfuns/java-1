package de.benjaminborbe.confluence.dao;

import de.benjaminborbe.confluence.api.ConfluenceInstanceIdentifier;
import de.benjaminborbe.tools.mapper.SingleMapBase;

public class ConfluenceInstanceIdentifierMapper<B> extends SingleMapBase<B, ConfluenceInstanceIdentifier> {

	public ConfluenceInstanceIdentifierMapper(final String name) {
		super(name);
	}

	@Override
	public String toString(final ConfluenceInstanceIdentifier value) {
		return value != null ? value.getId() : null;
	}

	@Override
	public ConfluenceInstanceIdentifier fromString(final String value) {
		return value != null ? new ConfluenceInstanceIdentifier(value) : null;
	}

}
