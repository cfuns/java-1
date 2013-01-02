package de.benjaminborbe.lucene.index.config;

import java.util.Collection;

import de.benjaminborbe.configuration.api.ConfigurationDescription;

public interface LuceneIndexConfig {

	String getIndexDirectory();

	Collection<ConfigurationDescription> getConfigurations();
}
