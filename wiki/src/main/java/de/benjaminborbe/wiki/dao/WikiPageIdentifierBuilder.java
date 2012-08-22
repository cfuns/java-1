package de.benjaminborbe.wiki.dao;

import de.benjaminborbe.api.IdentifierBuilder;
import de.benjaminborbe.wiki.api.WikiPageIdentifier;

public class WikiPageIdentifierBuilder implements IdentifierBuilder<String, WikiPageIdentifier> {

	@Override
	public WikiPageIdentifier buildIdentifier(final String value) {
		return new WikiPageIdentifier(value);
	}

}
