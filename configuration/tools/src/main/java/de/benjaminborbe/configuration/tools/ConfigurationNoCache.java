package de.benjaminborbe.configuration.tools;

import de.benjaminborbe.configuration.api.ConfigurationDescription;

public class ConfigurationNoCache implements ConfigurationCache {

	@Override
	public String get(final ConfigurationDescription configurationDescription) {
		return null;
	}

	@Override
	public void put(final ConfigurationDescription configurationDescription, final String s) {
	}

	@Override
	public void flush() {
	}
}
