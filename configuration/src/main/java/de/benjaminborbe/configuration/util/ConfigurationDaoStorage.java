package de.benjaminborbe.configuration.util;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

import de.benjaminborbe.configuration.api.ConfigurationIdentifier;
import de.benjaminborbe.storage.api.StorageService;
import de.benjaminborbe.storage.tools.DaoStorage;

@Singleton
public class ConfigurationDaoStorage extends DaoStorage<ConfigurationBean, ConfigurationIdentifier> implements ConfigurationDao {

	private static final String COLUMN_FAMILY = "configuration";

	@Inject
	public ConfigurationDaoStorage(
			final Logger logger,
			final StorageService storageService,
			final Provider<ConfigurationBean> beanProvider,
			final ConfigurationBeanMapper mapper,
			final ConfigurationIdentifierBuilder identifierBuilder) {
		super(logger, storageService, beanProvider, mapper, identifierBuilder);
	}

	@Override
	protected String getColumnFamily() {
		return COLUMN_FAMILY;
	}

}
