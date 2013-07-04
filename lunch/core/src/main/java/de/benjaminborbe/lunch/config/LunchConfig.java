package de.benjaminborbe.lunch.config;

import de.benjaminborbe.configuration.api.ConfigurationDescription;

import java.util.Collection;
import java.util.List;

public interface LunchConfig {

	Collection<ConfigurationDescription> getConfigurations();

	String getConfluenceUsername();

	String getConfluencePassword();

	String getConfluenceSpaceKey();

	List<String> getMittagNotifyKeywords();
}
