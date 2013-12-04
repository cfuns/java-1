package de.benjaminborbe.configuration.core.service;

import de.benjaminborbe.api.ValidationErrorSimple;
import de.benjaminborbe.api.ValidationException;
import de.benjaminborbe.configuration.api.ConfigurationDescription;
import de.benjaminborbe.configuration.api.ConfigurationIdentifier;
import de.benjaminborbe.configuration.api.ConfigurationServiceException;
import de.benjaminborbe.configuration.core.dao.ConfigurationBean;
import de.benjaminborbe.configuration.core.dao.ConfigurationDao;
import de.benjaminborbe.lib.validation.ValidationResultImpl;
import de.benjaminborbe.storage.api.StorageException;
import org.slf4j.Logger;

import javax.inject.Inject;

public class ConfigurationSetValueImpl implements ConfigurationSetValue {

	private final ConfigurationDao configurationDao;

	private final Logger logger;

	@Inject
	public ConfigurationSetValueImpl(
		final Logger logger, final ConfigurationDao configurationDao
	) {
		this.configurationDao = configurationDao;
		this.logger = logger;
	}

	@Override
	public void setConfigurationValue(
		final ConfigurationDescription configuration,
		final String value
	) throws ConfigurationServiceException, ValidationException {
		try {
			final ConfigurationIdentifier configurationIdentifier = configuration.getId();
			logger.debug("setConfigurationValue - key: " + configurationIdentifier + " value: " + value);

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
}
