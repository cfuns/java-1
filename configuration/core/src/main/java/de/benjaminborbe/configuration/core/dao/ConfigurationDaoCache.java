package de.benjaminborbe.configuration.core.dao;

import org.slf4j.Logger;

import javax.inject.Inject;
import com.google.inject.Provider;
import javax.inject.Singleton;

import de.benjaminborbe.configuration.api.ConfigurationIdentifier;
import de.benjaminborbe.storage.tools.DaoCache;

@Singleton
public class ConfigurationDaoCache extends DaoCache<ConfigurationBean, ConfigurationIdentifier> implements ConfigurationDao {

	@Inject
	public ConfigurationDaoCache(final Logger logger, final Provider<ConfigurationBean> provider) {
		super(logger, provider);
	}

}
