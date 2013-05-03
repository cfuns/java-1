package de.benjaminborbe.proxy.core.config;

import java.util.Collection;

import de.benjaminborbe.configuration.api.ConfigurationDescription;

public interface ProxyCoreConfig {

    Collection<ConfigurationDescription> getConfigurations();

    boolean conversationHistory();

    boolean conversationCrawler();

    boolean randomPort();

    Integer getPort();
}
