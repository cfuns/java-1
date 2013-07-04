package de.benjaminborbe.lucene.index.config;

import de.benjaminborbe.configuration.api.ConfigurationDescription;

import java.util.Collection;

public interface LuceneIndexConfig {

	String getLuceneIndexDirectory();

	Collection<ConfigurationDescription> getConfigurations();
}
