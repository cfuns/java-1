package de.benjaminborbe.configuration.core.service;

import de.benjaminborbe.api.ValidationErrorSimple;
import de.benjaminborbe.api.ValidationException;
import de.benjaminborbe.configuration.api.ConfigurationDescription;
import de.benjaminborbe.configuration.api.ConfigurationIdentifier;
import de.benjaminborbe.configuration.api.ConfigurationService;
import de.benjaminborbe.configuration.api.ConfigurationServiceException;
import de.benjaminborbe.configuration.core.dao.ConfigurationBean;
import de.benjaminborbe.configuration.core.dao.ConfigurationDao;
import de.benjaminborbe.configuration.core.dao.ConfigurationRegistry;
import de.benjaminborbe.storage.api.StorageException;
import de.benjaminborbe.tools.validation.ValidationResultImpl;
import org.slf4j.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Collection;

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
		return getConfigurationValue(getConfiguration(configurationIdentifier));
	}

	@Override
	public ConfigurationDescription getConfiguration(final ConfigurationIdentifier configurationIdentifier) throws ConfigurationServiceException {
		return configurationRegistry.get(configurationIdentifier);
	}

	@Override
	public String getConfigurationValue(final ConfigurationDescription configuration) throws ConfigurationServiceException {
		try {
			logger.trace("getConfigurationValue");
			final ConfigurationBean configurationBean = configurationDao.load(configuration.getId());
			if (configurationBean != null) {
				return configurationBean.getValue();
			} else {
				return configuration.getDefaultValueAsString();
			}
		} catch (final StorageException e) {
			return configuration.getDefaultValueAsString();
		}
	}

	@Override
	public void setConfigurationValue(
		final ConfigurationIdentifier configurationIdentifier,
		final String value
	) throws ConfigurationServiceException, ValidationException {
		try {
			logger.debug("setConfigurationValue - key: " + configurationIdentifier + " value: " + value);

			final ConfigurationDescription configuration = getConfiguration(configurationIdentifier);
			if (!configuration.validateValue(value)) {
				throw new ValidationException(new ValidationResultImpl(new ValidationErrorSimple("invalid value: " + value)));
			}

			ConfigurationBean configurationBean = configurationDao.load(configurationIdentifier);
			if (configurationBean == null) {
				configurationBean = configurationDao.create();
				configurationBean.setId(configurationIdentifier);
			}
			configurationBean.setValue(value);
			configurationDao.save(configurationBean);
		} catch (final StorageException e) {
			throw new ConfigurationServiceException(e.getClass().getSimpleName(), e);
		}
	}

	@Override
	public Collection<ConfigurationDescription> listConfigurations() {
		return configurationRegistry.getAll();
	}
}
