package de.benjaminborbe.search.config;

import java.util.Collection;

import de.benjaminborbe.configuration.api.ConfigurationDescription;

public interface SearchConfig {

	Collection<ConfigurationDescription> getConfigurations();

	boolean isUrlSearchActive();

}
