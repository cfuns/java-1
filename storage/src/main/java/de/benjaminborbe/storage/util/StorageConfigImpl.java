package de.benjaminborbe.storage.util;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import com.google.inject.Inject;

import de.benjaminborbe.configuration.api.Configuration;
import de.benjaminborbe.configuration.api.ConfigurationInteger;
import de.benjaminborbe.configuration.api.ConfigurationService;
import de.benjaminborbe.configuration.api.ConfigurationString;

public class StorageConfigImpl implements StorageConfig {

	private final ConfigurationString hostname = new ConfigurationString("localhost", "CassandraHost", "Hostname of CassandraServer");

	private final ConfigurationInteger port = new ConfigurationInteger(9160, "CassandraPort", "Port of CassandraServer");

	private final ConfigurationString keyspace = new ConfigurationString("bb", "CassandraKeyspace", "Keyspace of CassandraServer");

	private final ConfigurationString encoding = new ConfigurationString("UTF8", "CassandraEncoding", "Encoding of CassandraServer");

	private final ConfigurationService configurationService;

	@Inject
	public StorageConfigImpl(final ConfigurationService configurationService) {
		this.configurationService = configurationService;
	}

	@Override
	public String getHost() {
		return getValue(hostname);
	}

	@Override
	public int getPort() {
		return getValue(port);
	}

	@Override
	public String getKeySpace() {
		return getValue(keyspace);
	}

	@Override
	public String getEncoding() {
		return getValue(encoding);
	}

	private <T> T getValue(final Configuration<T> configuration) {
		// return configuration.getDefaultValue();
		return configurationService.getConfigurationValue(configuration);
	}

	@Override
	public Collection<Configuration<?>> getConfigurations() {
		final Set<Configuration<?>> result = new HashSet<Configuration<?>>();
		result.add(hostname);
		result.add(port);
		result.add(keyspace);
		result.add(encoding);
		return result;
	}

}
