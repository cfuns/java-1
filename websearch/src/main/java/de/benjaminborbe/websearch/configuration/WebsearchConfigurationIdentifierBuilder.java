package de.benjaminborbe.websearch.configuration;

import de.benjaminborbe.api.IdentifierBuilder;

public class WebsearchConfigurationIdentifierBuilder implements IdentifierBuilder<String, WebsearchConfigurationIdentifier> {

	@Override
	public WebsearchConfigurationIdentifier buildIdentifier(final String value) {
		return new WebsearchConfigurationIdentifier(value);
	}

}
