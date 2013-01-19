package de.benjaminborbe.storage.util;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class StorageConfigMock extends StorageConfigImpl implements StorageConfig {

	private int readLimit;

	private int maxConnections = 5;

	private String host;

	private static final String CASSANDRA_KEYSPACE = "test";

	@Inject
	public StorageConfigMock() {
		super();
	}

	@Override
	public String getKeySpace() {
		return CASSANDRA_KEYSPACE;
	}

	@Override
	public int getReadLimit() {
		if (readLimit == 0)
			return super.getReadLimit();
		return readLimit;
	}

	public void setReadLimit(final int readLimit) {
		this.readLimit = readLimit;
	}

	@Override
	public int getMaxConnections() {
		return maxConnections;
	}

	public void setMaxConnections(final int maxConnections) {
		this.maxConnections = maxConnections;
	}

	@Override
	public String getHost() {
		if (host != null) {
			return host;
		}
		return super.getHost();
	}

	public void setHost(final String host) {
		this.host = host;
	}

}
