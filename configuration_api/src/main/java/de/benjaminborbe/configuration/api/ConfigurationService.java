package de.benjaminborbe.configuration.api;

import java.util.Collection;

import de.benjaminborbe.api.ValidationException;

public interface ConfigurationService {

	String getConfigurationValue(ConfigurationIdentifier configurationIdentifier) throws ConfigurationServiceException;

	String getConfigurationValue(ConfigurationDescription configuration) throws ConfigurationServiceException;

	void setConfigurationValue(ConfigurationIdentifier configurationIdentifier, String value) throws ConfigurationServiceException, ValidationException;

	ConfigurationIdentifier createConfigurationIdentifier(String id) throws ConfigurationServiceException;

	Collection<ConfigurationDescription> listConfigurations() throws ConfigurationServiceException;

	ConfigurationDescription getConfiguration(ConfigurationIdentifier configurationIdentifier) throws ConfigurationServiceException;

}
