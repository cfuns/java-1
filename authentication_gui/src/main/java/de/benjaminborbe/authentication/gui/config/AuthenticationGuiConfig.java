package de.benjaminborbe.authentication.gui.config;

import java.util.Collection;

import de.benjaminborbe.configuration.api.ConfigurationDescription;

public interface AuthenticationGuiConfig {

	boolean registerEnabled();

	Collection<ConfigurationDescription> getConfigurations();
}
