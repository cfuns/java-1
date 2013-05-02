package de.benjaminborbe.configuration.tools;

import de.benjaminborbe.api.ValidationException;
import de.benjaminborbe.configuration.api.ConfigurationDescription;
import de.benjaminborbe.configuration.api.ConfigurationIdentifier;
import de.benjaminborbe.configuration.api.ConfigurationService;
import de.benjaminborbe.configuration.api.ConfigurationServiceException;
import de.benjaminborbe.lib.cache.Cache;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Collection;

@Singleton
public class ConfigurationServiceCache implements ConfigurationService {

	private final ConfigurationService configurationService;

	private final Cache<ConfigurationDescription, String> cache;

	@Inject
	public ConfigurationServiceCache(final ConfigurationService configurationService, final ConfigurationCache configurationCache) {
		this.configurationService = configurationService;
		this.cache = configurationCache;
	}

	@Override
	public String getConfigurationValue(final ConfigurationIdentifier configurationIdentifier) throws ConfigurationServiceException {
		return getConfigurationValue(getConfiguration(configurationIdentifier));
	}

	@Override
	public String getConfigurationValue(final ConfigurationDescription configurationDescription) throws ConfigurationServiceException {
		{
			final String value = cache.get(configurationDescription);
			if (value != null) {
				return value;
			}
		}
		final String value = configurationService.getConfigurationValue(configurationDescription);
		cache.put(configurationDescription, value);
		return value;
	}

	@Override
	public void setConfigurationValue(
		final ConfigurationIdentifier configurationIdentifier,
		final String value
	) throws ConfigurationServiceException, ValidationException {
		configurationService.setConfigurationValue(configurationIdentifier, value);
		cache.put(getConfiguration(configurationIdentifier), value);
	}

	@Override
	public ConfigurationIdentifier createConfigurationIdentifier(final String id) throws ConfigurationServiceException {
		return configurationService.createConfigurationIdentifier(id);
	}

	@Override
	public Collection<ConfigurationDescription> listConfigurations() throws ConfigurationServiceException {
		return configurationService.listConfigurations();
	}

	@Override
	public ConfigurationDescription getConfiguration(final ConfigurationIdentifier configurationIdentifier) throws ConfigurationServiceException {
		return configurationService.getConfiguration(configurationIdentifier);
	}

	public void flush() {
		cache.flush();
	}
}
