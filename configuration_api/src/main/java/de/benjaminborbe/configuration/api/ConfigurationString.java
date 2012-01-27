package de.benjaminborbe.configuration.api;

public class ConfigurationString extends ConfigurationBase<String> {

	public ConfigurationString(final String defaultValue, final String name, final String description) {
		super(defaultValue, name, description);
	}

	@Override
	public Class<String> getType() {
		return String.class;
	}

}
