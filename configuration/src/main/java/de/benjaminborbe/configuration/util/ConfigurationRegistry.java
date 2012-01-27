package de.benjaminborbe.configuration.util;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.configuration.api.Configuration;
import de.benjaminborbe.tools.util.RegistryImpl;

@Singleton
public class ConfigurationRegistry extends RegistryImpl<Configuration<?>> {

	@Inject
	public ConfigurationRegistry() {
	}

}
