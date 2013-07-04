package de.benjaminborbe.monitoring.config;

import de.benjaminborbe.configuration.api.ConfigurationDescription;

import java.util.Collection;

public interface MonitoringConfig {

	Collection<ConfigurationDescription> getConfigurations();

	boolean isCronEnabled();

	String getAuthToken();

}
