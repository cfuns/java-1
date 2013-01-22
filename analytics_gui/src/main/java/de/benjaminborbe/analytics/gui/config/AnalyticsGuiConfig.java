package de.benjaminborbe.analytics.gui.config;

import java.util.Collection;

import de.benjaminborbe.configuration.api.ConfigurationDescription;

public interface AnalyticsGuiConfig {

	Collection<ConfigurationDescription> getConfigurations();

	String getAuthToken();

}
