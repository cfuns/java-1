package de.benjaminborbe.lucene.index.config;

import java.util.Collection;

import de.benjaminborbe.configuration.api.ConfigurationDescription;

public interface LuceneIndexConfig {

	String getLuceneIndexDirectory();

	Collection<ConfigurationDescription> getConfigurations();
}
