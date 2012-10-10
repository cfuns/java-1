package de.benjaminborbe.configuration.service;

import java.util.Collection;
import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.configuration.api.ConfigurationDescription;
import de.benjaminborbe.configuration.api.ConfigurationIdentifier;
import de.benjaminborbe.configuration.api.ConfigurationService;
import de.benjaminborbe.configuration.api.ConfigurationServiceException;
import de.benjaminborbe.configuration.util.ConfigurationBean;
import de.benjaminborbe.configuration.util.ConfigurationDao;
import de.benjaminborbe.configuration.util.ConfigurationRegistry;
import de.benjaminborbe.storage.api.StorageException;

@Singleton
public class ConfigurationServiceImpl implements ConfigurationService {

	private final Logger logger;

	private final ConfigurationRegistry configurationRegistry;

	private final ConfigurationDao configurationDao;

	@Inject
	public ConfigurationServiceImpl(final Logger logger, final ConfigurationRegistry configurationRegistry, final ConfigurationDao configurationDao) {
		this.logger = logger;
		this.configurationRegistry = configurationRegistry;
		this.configurationDao = configurationDao;
	}

	@Override
	public ConfigurationIdentifier createConfigurationIdentifier(final String id) {
		return new ConfigurationIdentifier(id);
	}

	@Override
	public String getConfigurationValue(final ConfigurationIdentifier configurationIdentifier) throws ConfigurationServiceException {
		return getConfigurationValue(configurationRegistry.get(configurationIdentifier));
	}

	@Override
	public String getConfigurationValue(final ConfigurationDescription configuration) throws ConfigurationServiceException {
		logger.trace("getConfigurationValue");
		try {
			final ConfigurationBean configurationBean = configurationDao.load(configuration.getId());
			if (configurationBean != null) {
				return configurationBean.getValue();
			}
			else {
				return configuration.getDefaultValueAsString();
			}
		}
		catch (final StorageException e) {
			return configuration.getDefaultValueAsString();
		}
	}

	@Override
	public void setConfigurationValue(final ConfigurationIdentifier configurationIdentifier, final String value) throws ConfigurationServiceException {
		try {
			logger.trace("setConfigurationValue");
			ConfigurationBean configuration = configurationDao.load(configurationIdentifier);
			if (configuration == null) {
				configuration = configurationDao.create();
				configuration.setId(configurationIdentifier);
			}
			configuration.setValue(value);
			configurationDao.save(configuration);
		}
		catch (final StorageException e) {
			throw new ConfigurationServiceException(e.getClass().getSimpleName(), e);
		}
	}

	@Override
	public Collection<ConfigurationDescription> listConfigurations() {
		return configurationRegistry.getAll();
	}
}
