package de.benjaminborbe.storage.util;

import de.benjaminborbe.configuration.tools.ConfigurationServiceCache;
import de.benjaminborbe.storage.config.StorageConfig;
import de.benjaminborbe.storage.config.StorageConfigImpl;
import de.benjaminborbe.tools.util.ParseUtil;
import org.slf4j.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class StorageConfigMock extends StorageConfigImpl implements StorageConfig {

	private static final String CASSANDRA_KEYSPACE = "test";

	private int readLimit;

	private int maxConnections = 5;

	private String host;

	@Inject
	public StorageConfigMock(
		final Logger logger,
		final ParseUtil parseUtil,
		final ConfigurationServiceCache configurationServiceCache
	) {
		super(logger, parseUtil, configurationServiceCache);
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
