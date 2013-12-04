package de.benjaminborbe.configuration.core.service;

import de.benjaminborbe.configuration.api.ConfigurationDescription;
import de.benjaminborbe.configuration.api.ConfigurationServiceException;

public interface ConfigurationGetValue {

	String getConfigurationValue(ConfigurationDescription configuration) throws ConfigurationServiceException;
}
