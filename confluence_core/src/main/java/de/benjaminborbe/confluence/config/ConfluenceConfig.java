package de.benjaminborbe.confluence.config;

import java.util.Collection;

import de.benjaminborbe.configuration.api.ConfigurationDescription;

public interface ConfluenceConfig {

	Collection<ConfigurationDescription> getConfigurations();

	Integer getRefreshLimit();

	Boolean getCronEnabled();
}
