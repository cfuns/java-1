package de.benjaminborbe.microblog.gui.config;

import java.util.Collection;

import de.benjaminborbe.configuration.api.ConfigurationDescription;

public interface MicroblogGuiConfig {

	Collection<ConfigurationDescription> getConfigurations();

	String getAuthToken();

}
