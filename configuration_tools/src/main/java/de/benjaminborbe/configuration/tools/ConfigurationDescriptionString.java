package de.benjaminborbe.configuration.tools;

import de.benjaminborbe.configuration.api.ConfigurationDescription;
import de.benjaminborbe.configuration.api.ConfigurationIdentifier;

public class ConfigurationDescriptionString implements ConfigurationDescription {

	private final String name;

	private final String description;

	private final String defaultValue;

	public ConfigurationDescriptionString(final String defaultValue, final String name, final String description) {
		this.defaultValue = defaultValue;
		this.name = name;
		this.description = description;
	}

	@Override
	public ConfigurationIdentifier getId() {
		return new ConfigurationIdentifier(name);
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getDescription() {
		return description;
	}

	public String getDefaultValue() {
		return defaultValue;
	}

	@Override
	public String getDefaultValueAsString() {
		return defaultValue;
	}

	@Override
	public String getType() {
		return String.class.getSimpleName();
	}

}
