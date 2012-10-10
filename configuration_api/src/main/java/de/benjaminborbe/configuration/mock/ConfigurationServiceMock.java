package de.benjaminborbe.configuration.mock;

import java.util.List;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.configuration.api.ConfigurationDescription;
import de.benjaminborbe.configuration.api.ConfigurationIdentifier;
import de.benjaminborbe.configuration.api.ConfigurationService;
import de.benjaminborbe.configuration.api.ConfigurationServiceException;

@Singleton
public class ConfigurationServiceMock implements ConfigurationService {

	@Inject
	public ConfigurationServiceMock() {
	}

	@Override
	public String getConfigurationValue(final ConfigurationIdentifier configurationIdentifier) throws ConfigurationServiceException {
		return null;
	}

	@Override
	public void setConfigurationValue(final ConfigurationIdentifier configurationIdentifier, final String value) throws ConfigurationServiceException {
	}

	@Override
	public ConfigurationIdentifier createConfigurationIdentifier(final String id) {
		return null;
	}

	@Override
	public String getConfigurationValue(final ConfigurationDescription configuration) throws ConfigurationServiceException {
		return null;
	}

	@Override
	public List<ConfigurationDescription> listConfigurations() {
		return null;
	}

}
