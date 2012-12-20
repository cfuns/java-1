package de.benjaminborbe.projectile.config;

import java.util.Collection;

import de.benjaminborbe.configuration.api.ConfigurationDescription;

public interface ProjectileConfig {

	Collection<ConfigurationDescription> getConfigurations();

	String getAuthToken();
}
