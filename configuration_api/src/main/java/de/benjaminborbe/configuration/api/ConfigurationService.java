package de.benjaminborbe.configuration.api;

import java.util.Collection;

public interface ConfigurationService {

	String getConfigurationValue(ConfigurationIdentifier configurationIdentifier) throws ConfigurationServiceException;

	String getConfigurationValue(ConfigurationDescription configuration) throws ConfigurationServiceException;

	void setConfigurationValue(ConfigurationIdentifier configurationIdentifier, String value) throws ConfigurationServiceException;

	ConfigurationIdentifier createConfigurationIdentifier(String id) throws ConfigurationServiceException;

	Collection<ConfigurationDescription> listConfigurations();

}
