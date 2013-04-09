package de.benjaminborbe.shortener.dao;

import de.benjaminborbe.api.IdentifierBuilder;
import de.benjaminborbe.shortener.api.ShortenerUrlIdentifier;

public class ShortenerUrlIdentifierBuilder implements IdentifierBuilder<String, ShortenerUrlIdentifier> {

	@Override
	public ShortenerUrlIdentifier buildIdentifier(final String value) {
		return new ShortenerUrlIdentifier(value);
	}

}
