package de.benjaminborbe.lunch.config;

import java.util.Collection;

import de.benjaminborbe.configuration.api.ConfigurationDescription;

public interface LunchConfig {

	Collection<ConfigurationDescription> getConfigurations();

	String getConfluenceUsername();

	String getConfluencePassword();
}
