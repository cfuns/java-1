package de.benjaminborbe.authentication.gui.config;

import de.benjaminborbe.configuration.api.ConfigurationDescription;

import java.util.Collection;

public interface AuthenticationGuiConfig {

	boolean registerEnabled();

	Collection<ConfigurationDescription> getConfigurations();
}
