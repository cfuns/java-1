package de.benjaminborbe.monitoring.config;

import java.util.Collection;

import de.benjaminborbe.configuration.api.ConfigurationDescription;

public interface MonitoringConfig {

	Collection<ConfigurationDescription> getConfigurations();

	boolean isCronEnabled();

	String getAuthToken();

}
