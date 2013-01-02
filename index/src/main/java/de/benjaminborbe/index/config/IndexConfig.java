package de.benjaminborbe.index.config;

import java.util.Collection;

import de.benjaminborbe.configuration.api.ConfigurationDescription;

public interface IndexConfig {

	boolean getDistributedSearchEnabled();

	Collection<ConfigurationDescription> getConfigurations();
}
