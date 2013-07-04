package de.benjaminborbe.analytics.gui.config;

import de.benjaminborbe.configuration.api.ConfigurationDescription;

import java.util.Collection;

public interface AnalyticsGuiConfig {

	Collection<ConfigurationDescription> getConfigurations();

	String getAuthToken();

}
