package de.benjaminborbe.lunch.config;

import java.util.Collection;
import java.util.List;

import de.benjaminborbe.configuration.api.ConfigurationDescription;

public interface LunchConfig {

	Collection<ConfigurationDescription> getConfigurations();

	String getConfluenceUsername();

	String getConfluencePassword();

	String getConfluenceSpaceKey();

	List<String> getMittagNotifyKeywords();
}
