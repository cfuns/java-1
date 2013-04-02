package de.benjaminborbe.wiki.util;

import de.benjaminborbe.tools.mapper.Mapper;
import de.benjaminborbe.wiki.api.WikiSpaceIdentifier;

public class MapperWikiSpaceIdentifier implements Mapper<WikiSpaceIdentifier> {

	@Override
	public String toString(final WikiSpaceIdentifier value) {
		return value != null ? value.getId() : null;
	}

	@Override
	public WikiSpaceIdentifier fromString(final String value) {
		return value != null ? new WikiSpaceIdentifier(value) : null;
	}

}
