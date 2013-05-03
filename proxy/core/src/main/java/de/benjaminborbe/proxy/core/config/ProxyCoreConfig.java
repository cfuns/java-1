package de.benjaminborbe.proxy.core.config;

import de.benjaminborbe.configuration.api.ConfigurationDescription;

import java.util.Collection;

public interface ProxyCoreConfig {

	Collection<ConfigurationDescription> getConfigurations();

	boolean conversationHistory();

	boolean conversationCrawler();

	boolean randomPort();

	Integer getPort();

	boolean autoStart();
}
