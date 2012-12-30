package de.benjaminborbe.websearch.page;

import de.benjaminborbe.tools.mapper.Mapper;
import de.benjaminborbe.websearch.api.WebsearchPageIdentifier;

public class MapperWebsearchPageIdentifier implements Mapper<WebsearchPageIdentifier> {

	@Override
	public String toString(final WebsearchPageIdentifier value) {
		return value != null ? value.getId() : null;
	}

	@Override
	public WebsearchPageIdentifier fromString(final String value) {
		return value != null ? new WebsearchPageIdentifier(value) : null;
	}

}
