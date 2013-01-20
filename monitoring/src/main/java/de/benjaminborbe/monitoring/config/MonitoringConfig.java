package de.benjaminborbe.monitoring.config;

import java.util.Collection;

import de.benjaminborbe.configuration.api.ConfigurationDescription;

public interface MonitoringConfig {

	Collection<ConfigurationDescription> getConfigurations();

	String getAzUsername();

	String getAzPassword();

	String getTwentyfeetAdminUsername();

	String getTwentyfeetAdminPassword();

}
