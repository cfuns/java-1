package de.benjaminborbe.configuration.tools;

import de.benjaminborbe.configuration.api.ConfigurationDescription;
import de.benjaminborbe.configuration.api.ConfigurationIdentifier;

public class ConfigurationDescriptionDouble implements ConfigurationDescription {

	private final String name;

	private final String description;

	private final Double defaultValue;

	public ConfigurationDescriptionDouble(final Double defaultValue, final String name, final String description) {
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

	public Double getDefaultValue() {
		return defaultValue;
	}

	@Override
	public String getDefaultValueAsString() {
		return String.valueOf(defaultValue);
	}

	@Override
	public String getType() {
		return Double.class.getSimpleName();
	}

	@Override
	public boolean validateValue(final String value) {
		try {
			Double.parseDouble(value);
			return value != null;
		} catch (final Exception e) {
			return false;
		}
	}
}
