package de.benjaminborbe.poker.config;

import de.benjaminborbe.api.ValidationException;
import de.benjaminborbe.configuration.api.ConfigurationDescription;
import de.benjaminborbe.configuration.api.ConfigurationServiceException;

import java.util.Collection;

public interface PokerCoreConfig {

	boolean isJsonApiEnabled();

	String getJsonApiDashboardToken();

	Collection<ConfigurationDescription> getConfigurations();

	boolean isCronEnabled();

	void setCronEnabled(boolean enabled) throws ConfigurationServiceException, ValidationException;

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

	void setJsonApiEnabled(boolean enabled) throws ConfigurationServiceException, ValidationException;
}
