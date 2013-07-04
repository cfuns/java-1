package de.benjaminborbe.search.gui.config;

import de.benjaminborbe.configuration.api.ConfigurationDescription;

import java.util.Collection;

public interface SearchGuiConfig {

	Collection<ConfigurationDescription> getConfigurations();

	String getAuthToken();
}
