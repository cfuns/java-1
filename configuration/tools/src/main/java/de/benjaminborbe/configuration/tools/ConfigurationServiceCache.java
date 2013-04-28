package de.benjaminborbe.configuration.tools;

import de.benjaminborbe.api.ValidationException;
import de.benjaminborbe.configuration.api.ConfigurationDescription;
import de.benjaminborbe.configuration.api.ConfigurationIdentifier;
import de.benjaminborbe.configuration.api.ConfigurationService;
import de.benjaminborbe.configuration.api.ConfigurationServiceException;
import de.benjaminborbe.lib.cache.CacheSimple;

import javax.inject.Inject;
import java.util.Collection;

public class ConfigurationServiceCache implements ConfigurationService {

	private final ConfigurationService configurationService;

	private final CacheSimple<ConfigurationDescription, String> cache = new CacheSimple<ConfigurationDescription, String>();

	@Inject
	public ConfigurationServiceCache(final ConfigurationService configurationService) {
		this.configurationService = configurationService;
	}

	@Override
	public String getConfigurationValue(final ConfigurationIdentifier configurationIdentifier) throws ConfigurationServiceException {
		return getConfigurationValue(getConfiguration(configurationIdentifier));
	}

	@Override
	public String getConfigurationValue(final ConfigurationDescription configurationDescription) throws ConfigurationServiceException {
		String value = cache.get(configurationDescription);
		if (value != null) {
			return value;
		}
		return configurationService.getConfigurationValue(configurationDescription);
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
}
