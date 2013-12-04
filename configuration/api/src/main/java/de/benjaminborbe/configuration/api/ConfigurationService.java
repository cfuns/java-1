package de.benjaminborbe.configuration.api;

import de.benjaminborbe.api.ValidationException;

import java.util.Collection;

public interface ConfigurationService {

	String getConfigurationValue(ConfigurationIdentifier configurationIdentifier) throws ConfigurationServiceException;

	String getConfigurationValue(ConfigurationDescription configurationDescription) throws ConfigurationServiceException;

	void setConfigurationValue(ConfigurationIdentifier configurationIdentifier, String value) throws ConfigurationServiceException, ValidationException;

	ConfigurationIdentifier createConfigurationIdentifier(String id) throws ConfigurationServiceException;

	Collection<ConfigurationDescription> listConfigurations() throws ConfigurationServiceException;

	ConfigurationDescription getConfiguration(ConfigurationIdentifier configurationIdentifier) throws ConfigurationServiceException;

	void setConfigurationValue(
		ConfigurationDescription configurationDescription,
		String value
	) throws ConfigurationServiceException, ValidationException;
}
