package de.benjaminborbe.confluence.dao;

import de.benjaminborbe.api.IdentifierBuilder;
import de.benjaminborbe.confluence.api.ConfluenceInstanceIdentifier;

public class ConfluenceInstanceIdentifierBuilder implements IdentifierBuilder<String, ConfluenceInstanceIdentifier> {

	@Override
	public ConfluenceInstanceIdentifier buildIdentifier(final String value) {
		return new ConfluenceInstanceIdentifier(value);
	}

}
