package de.benjaminborbe.cron.config;

import de.benjaminborbe.configuration.api.ConfigurationDescription;

import java.util.Collection;

public interface CronConfig {

	Collection<ConfigurationDescription> getConfigurations();

	boolean autoStart();

	boolean isRemoteCronAllowed();
}
