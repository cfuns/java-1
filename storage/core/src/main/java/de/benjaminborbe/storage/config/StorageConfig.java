package de.benjaminborbe.storage.config;

import de.benjaminborbe.configuration.api.ConfigurationDescription;

import java.util.Collection;

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
