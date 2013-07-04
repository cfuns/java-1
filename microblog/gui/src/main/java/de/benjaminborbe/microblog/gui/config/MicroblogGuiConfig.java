package de.benjaminborbe.microblog.gui.config;

import de.benjaminborbe.configuration.api.ConfigurationDescription;

import java.util.Collection;

public interface MicroblogGuiConfig {

	Collection<ConfigurationDescription> getConfigurations();

	String getAuthToken();

}
