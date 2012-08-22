package de.benjaminborbe.wiki.dao;

import de.benjaminborbe.api.IdentifierBuilder;
import de.benjaminborbe.wiki.api.WikiSpaceIdentifier;

public class WikiSpaceIdentifierBuilder implements IdentifierBuilder<String, WikiSpaceIdentifier> {

	@Override
	public WikiSpaceIdentifier buildIdentifier(final String value) {
		return new WikiSpaceIdentifier(value);
	}

}
