package de.benjaminborbe.configuration.dao;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

import de.benjaminborbe.configuration.api.ConfigurationIdentifier;
import de.benjaminborbe.storage.tools.DaoCache;

@Singleton
public class ConfigurationDaoCache extends DaoCache<ConfigurationBean, ConfigurationIdentifier> implements ConfigurationDao {

	@Inject
	public ConfigurationDaoCache(final Logger logger, final Provider<ConfigurationBean> provider) {
		super(logger, provider);
	}

}
