package de.benjaminborbe.websearch.config;

import java.util.Collection;

import de.benjaminborbe.configuration.api.ConfigurationDescription;

public interface WebsearchConfig {

	Collection<ConfigurationDescription> getConfigurations();

	Integer getRefreshLimit();

	Boolean getCronEnabled();
}
