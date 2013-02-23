package de.benjaminborbe.poker.config;

import java.util.Collection;

import de.benjaminborbe.configuration.api.ConfigurationDescription;

public interface PokerConfig {

	Collection<ConfigurationDescription> getConfigurations();

	boolean isCronEnabled();

	long getAutoFoldTimeout();

	long getMaxBid();
}
