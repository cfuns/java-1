package de.benjaminborbe.confluence.util;

import de.benjaminborbe.confluence.api.ConfluencePageIdentifier;
import de.benjaminborbe.tools.mapper.Mapper;

public class MapperConfluencePageIdentifier implements Mapper<ConfluencePageIdentifier> {

	@Override
	public String toString(final ConfluencePageIdentifier value) {
		return value != null ? value.getId() : null;
	}

	@Override
	public ConfluencePageIdentifier fromString(final String value) {
		return value != null ? new ConfluencePageIdentifier(value) : null;
	}

}
