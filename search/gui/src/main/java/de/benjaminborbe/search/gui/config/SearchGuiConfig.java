package de.benjaminborbe.search.gui.config;

import java.util.Collection;

import de.benjaminborbe.configuration.api.ConfigurationDescription;

public interface SearchGuiConfig {

	Collection<ConfigurationDescription> getConfigurations();

	String getAuthToken();
}
