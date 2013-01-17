package de.benjaminborbe.lunch.gui.config;

import java.util.Collection;

import de.benjaminborbe.configuration.api.ConfigurationDescription;

public interface LunchGuiConfig {

	Collection<ConfigurationDescription> getConfigurations();

	String getAuthToken();

}
