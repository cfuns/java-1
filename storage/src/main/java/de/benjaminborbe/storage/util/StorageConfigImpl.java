package de.benjaminborbe.storage.util;

public class StorageConfigImpl implements StorageConfig {

	private final String CASSANDRA_HOST = "localhost";

	private final int CASSANDRA_PORT = 9160;

	private final String CASSANDRA_KEYSPACE = "bb";

	private final String CASSANDRA_ENCODING = "UTF8";

	@Override
	public String getHost() {
		return CASSANDRA_HOST;
	}

	@Override
	public int getPort() {
		return CASSANDRA_PORT;
	}

	@Override
	public String getKeySpace() {
		return CASSANDRA_KEYSPACE;
	}

	@Override
	public String getEncoding() {
		return CASSANDRA_ENCODING;
	}

}
