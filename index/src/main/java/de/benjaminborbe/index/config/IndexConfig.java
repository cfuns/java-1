package de.benjaminborbe.index.config;

import java.util.Collection;

import de.benjaminborbe.configuration.api.ConfigurationDescription;

public interface IndexConfig {

	boolean isIndexDistributedEnabled();

	boolean isIndexLuceneEnabled();

	Collection<ConfigurationDescription> getConfigurations();
}
