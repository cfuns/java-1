package de.benjaminborbe.configuration.core.dao;

import de.benjaminborbe.api.IdentifierBuilder;
import de.benjaminborbe.configuration.api.ConfigurationIdentifier;

public class ConfigurationIdentifierBuilder implements IdentifierBuilder<String, ConfigurationIdentifier> {

	@Override
	public ConfigurationIdentifier buildIdentifier(final String value) {
		return new ConfigurationIdentifier(value);
	}

}
