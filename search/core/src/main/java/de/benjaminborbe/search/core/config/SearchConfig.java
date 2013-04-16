package de.benjaminborbe.search.core.config;

import de.benjaminborbe.configuration.api.ConfigurationDescription;

import java.util.Collection;

public interface SearchConfig {

	Collection<ConfigurationDescription> getConfigurations();

	boolean isUrlSearchActive();

}
