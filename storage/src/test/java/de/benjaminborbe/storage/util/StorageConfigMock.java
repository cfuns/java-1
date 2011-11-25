package de.benjaminborbe.storage.util;

public class StorageConfigMock extends StorageConfigImpl implements StorageConfig {

	private static final String CASSANDRA_KEYSPACE = "bb_test";

	@Override
	public String getKeySpace() {
		return CASSANDRA_KEYSPACE;
	}
}
