package de.benjaminborbe.confluence.util;

import de.benjaminborbe.confluence.api.ConfluenceInstanceIdentifier;
import de.benjaminborbe.tools.mapper.Mapper;

public class MapperConfluenceInstanceIdentifier implements Mapper<ConfluenceInstanceIdentifier> {

	@Override
	public String toString(final ConfluenceInstanceIdentifier value) {
		return value != null ? value.getId() : null;
	}

	@Override
	public ConfluenceInstanceIdentifier fromString(final String value) {
		return value != null ? new ConfluenceInstanceIdentifier(value) : null;
	}

}
