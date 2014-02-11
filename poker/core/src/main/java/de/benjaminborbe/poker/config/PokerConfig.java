package de.benjaminborbe.poker.config;

import de.benjaminborbe.configuration.api.ConfigurationDescription;

import java.util.Collection;

public interface PokerConfig {

	Collection<ConfigurationDescription> getConfigurations();

	boolean isCronEnabled();

	long getAutoFoldTimeout();

	long getMaxBid();

	boolean isCreditsNegativeAllowed();

	/*
	 * disabled: < 1
	 * enabled: > 1
	 */
	double getMinRaiseFactor();

	/*
	 * disabled: < 1
	 * enabled: > 1
	 */
	double getMaxRaiseFactor();

	String getScheduleExpression();
}
