package de.benjaminborbe.analytics.config;

import java.util.Collection;

import de.benjaminborbe.configuration.api.ConfigurationDescription;

public interface AnalyticsConfig {

	Collection<ConfigurationDescription> getConfigurations();

	Boolean getCronActive();

	Boolean getDeleteLog();

}
