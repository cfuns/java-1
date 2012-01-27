package de.benjaminborbe.configuration.api;

public class ConfigurationInteger extends ConfigurationBase<Integer> {

	public ConfigurationInteger(final Integer defaultValue, final String name, final String description) {
		super(defaultValue, name, description);
	}

	@Override
	public Class<Integer> getType() {
		return Integer.class;
	}

}
