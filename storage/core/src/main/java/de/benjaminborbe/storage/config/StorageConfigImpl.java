package de.benjaminborbe.storage.config;

import de.benjaminborbe.configuration.api.ConfigurationDescription;
import de.benjaminborbe.configuration.tools.ConfigurationBase;
import de.benjaminborbe.configuration.tools.ConfigurationDescriptionInteger;
import de.benjaminborbe.configuration.tools.ConfigurationDescriptionString;
import de.benjaminborbe.configuration.tools.ConfigurationServiceCache;
import de.benjaminborbe.tools.util.ParseUtil;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class StorageConfigImpl extends ConfigurationBase implements StorageConfig {

	private final ConfigurationDescriptionString hostname = new ConfigurationDescriptionString("cassandra", "CassandraHost", "Hostname of CassandraServer");

	private final ConfigurationDescriptionInteger port = new ConfigurationDescriptionInteger(9160, "CassandraPort", "Port of CassandraServer");

	private final ConfigurationDescriptionString keyspace = new ConfigurationDescriptionString("bb_test", "CassandraKeyspace", "Keyspace of CassandraServer");

	private final ConfigurationDescriptionString encoding = new ConfigurationDescriptionString("UTF8", "CassandraEncoding", "Encoding of CassandraServer");

	private final ConfigurationDescriptionInteger readLimit = new ConfigurationDescriptionInteger(10000, "CassandraReadLimit", "ReadLimit of CassandraServer");

	private final ConfigurationDescriptionInteger maxConnections = new ConfigurationDescriptionInteger(50, "CassandraMaxConnections", "MaxConnections to CassandraServer");

	private final ConfigurationDescriptionInteger socketTimeout = new ConfigurationDescriptionInteger(10000, "CassandraSocketTimeout", "SocketTimeout to CassandraServer");

	private final ConfigurationDescriptionString backupDirectory = new ConfigurationDescriptionString("/tmp", "CassandraBackupDirectory", "BackupDirectory of CassandraServer");

	@Inject
	public StorageConfigImpl(
		final Logger logger,
		final ParseUtil parseUtil,
		final ConfigurationServiceCache configurationServiceCache
	) {
		super(logger, parseUtil, configurationServiceCache);
	}

	@Override
	public String getHost() {
		return getValueString(hostname);
	}

	@Override
	public int getPort() {
		return getValueInteger(port);
	}

	@Override
	public String getKeySpace() {
		return getValueString(keyspace);
	}

	@Override
	public String getEncoding() {
		return getValueString(encoding);
	}

	@Override
	public int getReadLimit() {
		return getValueInteger(readLimit);
	}

	@Override
	public Collection<ConfigurationDescription> getConfigurations() {
		final Set<ConfigurationDescription> result = new HashSet<ConfigurationDescription>();
		result.add(hostname);
		result.add(port);
		result.add(keyspace);
		result.add(encoding);
		return result;
	}

	@Override
	public int getMaxConnections() {
		return getValueInteger(maxConnections);
	}

	@Override
	public int getSocketTimeout() {
		return getValueInteger(socketTimeout);
	}

	@Override
	public boolean getAliveCheck() {
		return true;
	}

	@Override
	public String getBackpuDirectory() {
		return getValueString(backupDirectory);
	}

}
