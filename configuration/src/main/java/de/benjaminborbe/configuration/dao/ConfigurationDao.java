package de.benjaminborbe.configuration.dao;

import de.benjaminborbe.configuration.api.ConfigurationIdentifier;
import de.benjaminborbe.storage.tools.Dao;

public interface ConfigurationDao extends Dao<ConfigurationBean, ConfigurationIdentifier> {
}
