package de.benjaminborbe.confluence.config;

import de.benjaminborbe.configuration.api.ConfigurationDescription;

import java.util.Collection;

public interface ConfluenceConfig {

	Collection<ConfigurationDescription> getConfigurations();

	Integer getRefreshLimit();

	Boolean getCronEnabled();
}
