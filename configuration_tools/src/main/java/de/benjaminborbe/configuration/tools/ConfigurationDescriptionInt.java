package de.benjaminborbe.configuration.tools;

import de.benjaminborbe.configuration.api.ConfigurationDescription;
import de.benjaminborbe.configuration.api.ConfigurationIdentifier;

public class ConfigurationDescriptionInt implements ConfigurationDescription {

	private final String name;

	private final String description;

	private final int defaultValue;

	public ConfigurationDescriptionInt(final int defaultValue, final String name, final String description) {
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

	public int getDefaultValue() {
		return defaultValue;
	}

	@Override
	public String getDefaultValueAsString() {
		return String.valueOf(defaultValue);
	}

	@Override
	public String getType() {
		return Integer.class.getSimpleName();
	}
}
