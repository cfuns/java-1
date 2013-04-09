package de.benjaminborbe.shortener.dao;

import de.benjaminborbe.shortener.api.ShortenerUrlIdentifier;
import de.benjaminborbe.tools.mapper.Mapper;

public class MapperShortenerUrlIdentifier implements Mapper<ShortenerUrlIdentifier> {

	@Override
	public String toString(final ShortenerUrlIdentifier value) {
		return value != null ? value.getId() : null;
	}

	@Override
	public ShortenerUrlIdentifier fromString(final String value) {
		return value != null ? new ShortenerUrlIdentifier(value) : null;
	}

}
