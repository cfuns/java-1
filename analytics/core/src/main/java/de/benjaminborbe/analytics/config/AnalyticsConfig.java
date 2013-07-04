package de.benjaminborbe.analytics.config;

import de.benjaminborbe.configuration.api.ConfigurationDescription;

import java.util.Collection;

public interface AnalyticsConfig {

	Collection<ConfigurationDescription> getConfigurations();

	Boolean getCronActive();

	long getAggregationChunkSize();

}
