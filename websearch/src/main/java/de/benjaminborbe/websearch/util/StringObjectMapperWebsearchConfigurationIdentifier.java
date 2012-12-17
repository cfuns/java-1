package de.benjaminborbe.websearch.util;

import de.benjaminborbe.tools.mapper.stringobject.StringObjectMapperBase;
import de.benjaminborbe.websearch.api.WebsearchConfigurationIdentifier;

public class StringObjectMapperWebsearchConfigurationIdentifier<B> extends StringObjectMapperBase<B, WebsearchConfigurationIdentifier> {

	public StringObjectMapperWebsearchConfigurationIdentifier(final String name) {
		super(name);
	}

	@Override
	public String toString(final WebsearchConfigurationIdentifier value) {
		return value != null ? value.getId() : null;
	}

	@Override
	public WebsearchConfigurationIdentifier fromString(final String value) {
		return value != null ? new WebsearchConfigurationIdentifier(value) : null;
	}

}
