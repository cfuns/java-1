package de.benjaminborbe.configuration.core.service;

import de.benjaminborbe.configuration.api.ConfigurationDescription;
import de.benjaminborbe.configuration.api.ConfigurationServiceException;
import de.benjaminborbe.configuration.core.dao.ConfigurationBean;
import de.benjaminborbe.configuration.core.dao.ConfigurationDao;
import de.benjaminborbe.storage.api.StorageException;
import org.slf4j.Logger;

import javax.inject.Inject;

public class ConfigurationGetValueImpl implements ConfigurationGetValue {

	private final ConfigurationDao configurationDao;

	private final Logger logger;

	@Inject
	public ConfigurationGetValueImpl(final Logger logger, final ConfigurationDao configurationDao) {
		this.configurationDao = configurationDao;
		this.logger = logger;
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

}
