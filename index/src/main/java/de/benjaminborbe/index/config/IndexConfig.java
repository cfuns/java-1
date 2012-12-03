package de.benjaminborbe.index.config;

import java.util.Collection;

import de.benjaminborbe.configuration.api.ConfigurationDescription;

public interface IndexConfig {

	String getIndexDirectory();

	Collection<ConfigurationDescription> getConfigurations();
}
