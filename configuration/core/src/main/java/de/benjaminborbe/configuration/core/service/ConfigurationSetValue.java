package de.benjaminborbe.configuration.core.service;

import de.benjaminborbe.api.ValidationException;
import de.benjaminborbe.cache.api.CacheServiceException;
import de.benjaminborbe.configuration.api.ConfigurationDescription;
import de.benjaminborbe.configuration.api.ConfigurationServiceException;

public interface ConfigurationSetValue {

	void setConfigurationValue(
		ConfigurationDescription configuration,
		String value
	) throws CacheServiceException, ConfigurationServiceException, ValidationException;

}
