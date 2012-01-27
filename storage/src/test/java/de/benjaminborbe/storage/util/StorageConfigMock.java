package de.benjaminborbe.storage.util;

import com.google.inject.Inject;

import de.benjaminborbe.configuration.api.ConfigurationService;

public class StorageConfigMock extends StorageConfigImpl implements StorageConfig {

	@Inject
	public StorageConfigMock(final ConfigurationService configurationService) {
		super(configurationService);
	}

	private static final String CASSANDRA_KEYSPACE = "bb_test";

	@Override
	public String getKeySpace() {
		return CASSANDRA_KEYSPACE;
	}
}
