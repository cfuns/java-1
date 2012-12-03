package de.benjaminborbe.confluence.dao;

import de.benjaminborbe.confluence.api.ConfluencePageIdentifier;
import de.benjaminborbe.tools.mapper.SingleMapBase;

public class ConfluencePageIdentifierMapper<B> extends SingleMapBase<B, ConfluencePageIdentifier> {

	public ConfluencePageIdentifierMapper(final String name) {
		super(name);
	}

	@Override
	public String toString(final ConfluencePageIdentifier value) {
		return value != null ? value.getId() : null;
	}

	@Override
	public ConfluencePageIdentifier fromString(final String value) {
		return value != null ? new ConfluencePageIdentifier(value) : null;
	}

}
