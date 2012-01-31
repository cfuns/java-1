package de.benjaminborbe.configuration.service;

import java.util.Collection;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.configuration.api.Configuration;
import de.benjaminborbe.configuration.api.ConfigurationService;
import de.benjaminborbe.configuration.util.ConfigurationBean;
import de.benjaminborbe.configuration.util.ConfigurationDao;
import de.benjaminborbe.configuration.util.ConfigurationRegistry;

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
	public Collection<Configuration<?>> listConfigurations() {
		logger.trace("listConfigurations");
		final Collection<Configuration<?>> result = configurationRegistry.getAll();
		logger.trace("found " + result.size() + " configurations");
		return result;
	}

	@Override
	public <T> T getConfigurationValue(final Configuration<T> configuration) {
		logger.trace("getConfigurationValue");
		return configuration.getDefaultValue();
	}

	@Override
	public <T> void setConfigurationValue(final Configuration<T> configuration, final T value) {
		logger.trace("setConfigurationValue");
		ConfigurationBean configurationBean = configurationDao.findByConfiguration(configuration);
		if (configurationBean == null) {
			configurationBean = configurationDao.create();
			configurationBean.setKey(configuration.getName());
		}
		configurationBean.setValue(String.valueOf(value));
		configurationDao.save(configurationBean);
	}
}
