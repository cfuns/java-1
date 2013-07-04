package de.benjaminborbe.lunch.gui.config;

import de.benjaminborbe.configuration.api.ConfigurationDescription;

import java.util.Collection;

public interface LunchGuiConfig {

	Collection<ConfigurationDescription> getConfigurations();

	String getAuthToken();

}
