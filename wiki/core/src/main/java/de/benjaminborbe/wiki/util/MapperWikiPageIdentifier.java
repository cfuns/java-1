package de.benjaminborbe.wiki.util;

import de.benjaminborbe.tools.mapper.Mapper;
import de.benjaminborbe.wiki.api.WikiPageIdentifier;

public class MapperWikiPageIdentifier implements Mapper<WikiPageIdentifier> {

	@Override
	public String toString(final WikiPageIdentifier value) {
		return value != null ? value.getId() : null;
	}

	@Override
	public WikiPageIdentifier fromString(final String value) {
		return value != null ? new WikiPageIdentifier(value) : null;
	}

}
