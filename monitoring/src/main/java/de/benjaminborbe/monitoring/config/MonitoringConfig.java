package de.benjaminborbe.monitoring.config;

import java.util.Collection;

import de.benjaminborbe.configuration.api.ConfigurationDescription;

public interface MonitoringConfig {

	String getAzUsername();

	String getAzPassword();

	Collection<ConfigurationDescription> getConfigurations();

	String getTwentyfeetAdminUsername();

	String getTwentyfeetAdminPassword();

}
