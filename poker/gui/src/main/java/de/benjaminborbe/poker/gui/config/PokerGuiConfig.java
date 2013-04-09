package de.benjaminborbe.poker.gui.config;

import java.util.Collection;

import de.benjaminborbe.configuration.api.ConfigurationDescription;

public interface PokerGuiConfig {

	Collection<ConfigurationDescription> getConfigurations();

	boolean isJsonApiEnabled();

}
