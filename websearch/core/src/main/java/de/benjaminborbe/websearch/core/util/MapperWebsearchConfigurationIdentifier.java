package de.benjaminborbe.websearch.core.util;

import de.benjaminborbe.tools.mapper.Mapper;
import de.benjaminborbe.websearch.api.WebsearchConfigurationIdentifier;

public class MapperWebsearchConfigurationIdentifier implements Mapper<WebsearchConfigurationIdentifier> {

	@Override
	public String toString(final WebsearchConfigurationIdentifier value) {
		return value != null ? value.getId() : null;
	}

	@Override
	public WebsearchConfigurationIdentifier fromString(final String value) {
		return value != null ? new WebsearchConfigurationIdentifier(value) : null;
	}

}
