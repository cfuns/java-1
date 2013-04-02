package de.benjaminborbe.confluence.dao;

import de.benjaminborbe.api.IdentifierBuilder;
import de.benjaminborbe.confluence.api.ConfluencePageIdentifier;

public class ConfluencePageIdentifierBuilder implements IdentifierBuilder<String, ConfluencePageIdentifier> {

	@Override
	public ConfluencePageIdentifier buildIdentifier(final String value) {
		return new ConfluencePageIdentifier(value);
	}

}
