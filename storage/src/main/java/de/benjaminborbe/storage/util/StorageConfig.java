package de.benjaminborbe.storage.util;

import java.util.Collection;

import de.benjaminborbe.configuration.api.ConfigurationDescription;

public interface StorageConfig {

	int getReadLimit();

	String getHost();

	int getPort();

	String getKeySpace();

	String getEncoding();

	Collection<ConfigurationDescription> getConfigurations();

	int getMaxConnections();

	int getSocketTimeout();

	boolean getAliveCheck();

	String getBackpuDirectory();
}
