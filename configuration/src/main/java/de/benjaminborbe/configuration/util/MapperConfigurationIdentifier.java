package de.benjaminborbe.configuration.util;

import de.benjaminborbe.configuration.api.ConfigurationIdentifier;
import de.benjaminborbe.tools.mapper.Mapper;

public class MapperConfigurationIdentifier implements Mapper<ConfigurationIdentifier> {

	@Override
	public String toString(final ConfigurationIdentifier value) {
		return value != null ? value.getId() : null;
	}

	@Override
	public ConfigurationIdentifier fromString(final String value) {
		return value != null ? new ConfigurationIdentifier(value) : null;
	}

}
