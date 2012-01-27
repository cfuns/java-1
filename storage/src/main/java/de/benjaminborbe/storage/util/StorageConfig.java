package de.benjaminborbe.storage.util;

import java.util.Collection;

import de.benjaminborbe.configuration.api.Configuration;

public interface StorageConfig {

	String getHost();

	int getPort();

	String getKeySpace();

	String getEncoding();

	Collection<Configuration<?>> getConfigurations();
}
