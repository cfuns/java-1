package de.benjaminborbe.projectile.config;

import de.benjaminborbe.configuration.api.ConfigurationDescription;

import java.util.Collection;

public interface ProjectileConfig {

	Collection<ConfigurationDescription> getConfigurations();

	String getAuthToken();

	Boolean getCronActive();

	String getPop3Login();

	String getPop3Password();

	String getPop3Hostname();

	boolean getPop3Delete();
}
