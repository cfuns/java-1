package de.benjaminborbe.configuration.util;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

import de.benjaminborbe.configuration.api.Configuration;
import de.benjaminborbe.tools.dao.DaoCacheAutoIncrement;
import de.benjaminborbe.tools.util.IdGeneratorLong;

@Singleton
public class ConfigurationDaoImpl extends DaoCacheAutoIncrement<ConfigurationBean> implements ConfigurationDao {

	@Inject
	public ConfigurationDaoImpl(final Logger logger, final IdGeneratorLong idGenerator, final Provider<ConfigurationBean> provider) {
		super(logger, idGenerator, provider);
	}

	@Override
	public ConfigurationBean findByConfiguration(final Configuration<?> configuration) {
		for (final ConfigurationBean configurationBean : getAll()) {
			if (configurationBean.getKey().equals(configuration.getName())) {
				return configurationBean;
			}
		}
		return null;
	}

}
