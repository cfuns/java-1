package de.benjaminborbe.configuration.api;

import java.util.Collection;

public interface ConfigurationService {

	/**
	 * Returns all required Configurations
	 */
	Collection<Configuration<?>> listConfigurations();

	/**
	 * Return the value
	 */
	<T> T getConfigurationValue(Configuration<T> configuration);

	/**
	 * Define new value for configuration
	 */
	<T> void setConfigurationValue(Configuration<T> configuration, T value);
}
