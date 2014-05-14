package de.benjaminborbe.poker.table.server.config;

import de.benjaminborbe.configuration.api.ConfigurationDescription;

import java.util.Collection;

public interface PokerTableConfig {

	Collection<ConfigurationDescription> getConfigurations();

	String getJsonApiDashboardToken();
}
