package de.benjaminborbe.index.config;

import de.benjaminborbe.configuration.api.ConfigurationDescription;

import java.util.Collection;

public interface IndexConfig {

	boolean isIndexDistributedEnabled();

	boolean isIndexLuceneEnabled();

	Collection<ConfigurationDescription> getConfigurations();
}
