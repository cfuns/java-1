package de.benjaminborbe.configuration.util;

import de.benjaminborbe.configuration.api.Configuration;
import de.benjaminborbe.tools.dao.Dao;

public interface ConfigurationDao extends Dao<ConfigurationBean, Long> {

	ConfigurationBean findByConfiguration(Configuration<?> configuration);
}
