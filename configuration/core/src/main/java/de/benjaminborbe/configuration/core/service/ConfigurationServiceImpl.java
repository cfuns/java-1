package de.benjaminborbe.configuration.core.service;

import de.benjaminborbe.api.ValidationException;
import de.benjaminborbe.cache.api.CacheServiceException;
import de.benjaminborbe.configuration.api.ConfigurationDescription;
import de.benjaminborbe.configuration.api.ConfigurationIdentifier;
import de.benjaminborbe.configuration.api.ConfigurationService;
import de.benjaminborbe.configuration.api.ConfigurationServiceException;
import de.benjaminborbe.configuration.core.dao.ConfigurationRegistry;
import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Collection;

@Singleton
public class ConfigurationServiceImpl implements ConfigurationService {

	private final ConfigurationRegistry configurationRegistry;

	private final ConfigurationSetValue configurationSetValue;

	private final ConfigurationGetValue configurationGetValue;

	@Inject
	public ConfigurationServiceImpl(
			final ConfigurationRegistry configurationRegistry,
			final ConfigurationSetValue configurationSetValue,
			final ConfigurationGetValue configurationGetValue) {
		this.configurationRegistry = configurationRegistry;
		this.configurationSetValue = configurationSetValue;
		this.configurationGetValue = configurationGetValue;
	}

	@Override
	public ConfigurationIdentifier createConfigurationIdentifier(final String id) {
		return new ConfigurationIdentifier(id);
	}

	@Override
	public String getConfigurationValue(final ConfigurationIdentifier configurationIdentifier) throws ConfigurationServiceException {
		return getConfigurationValue(getConfiguration(configurationIdentifier));
	}

	@Override
	public String getConfigurationValue(final ConfigurationDescription configurationDescription) throws ConfigurationServiceException {
		try {
			return configurationGetValue.getConfigurationValue(configurationDescription);
		}
		catch (final CacheServiceException e) {
			throw new ConfigurationServiceException(e);
		}
	}

	@Override
	public void setConfigurationValue(final ConfigurationIdentifier configurationIdentifier, final String value) throws ConfigurationServiceException, ValidationException {
		final ConfigurationDescription configurationDescription = configurationRegistry.get(configurationIdentifier);
		setConfigurationValue(configurationDescription, value);
	}

	@Override
	public void setConfigurationValue(final ConfigurationDescription configurationDescription, final String value) throws ConfigurationServiceException, ValidationException {
		try {
			configurationSetValue.setConfigurationValue(configurationDescription, value);
		}
		catch (final CacheServiceException e) {
			throw new ConfigurationServiceException(e);
		}
	}

	@Override
	public ConfigurationDescription getConfiguration(final ConfigurationIdentifier configurationIdentifier) throws ConfigurationServiceException {
		return configurationRegistry.get(configurationIdentifier);
	}

	@Override
	public Collection<ConfigurationDescription> listConfigurations() {
		return configurationRegistry.getAll();
	}
}
