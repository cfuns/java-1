package de.benjaminborbe.proxy.core.config;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.inject.Inject;

import org.slf4j.Logger;

import de.benjaminborbe.configuration.api.ConfigurationDescription;
import de.benjaminborbe.configuration.tools.ConfigurationBase;
import de.benjaminborbe.configuration.tools.ConfigurationDescriptionBoolean;
import de.benjaminborbe.configuration.tools.ConfigurationDescriptionInteger;
import de.benjaminborbe.configuration.tools.ConfigurationServiceCache;
import de.benjaminborbe.tools.util.ParseUtil;

public class ProxyCoreConfigImpl extends ConfigurationBase implements ProxyCoreConfig {

    private final ConfigurationDescriptionBoolean conversationHistory = new ConfigurationDescriptionBoolean(false, "ProxyConversationHistory",
            "Proxy Conversation History");

    private final ConfigurationDescriptionBoolean conversationCrawler = new ConfigurationDescriptionBoolean(false, "ProxyConversationCrawler",
            "Proxy Conversation Crawler");

    private final ConfigurationDescriptionBoolean randomPort = new ConfigurationDescriptionBoolean(true, "ProxyRandomPort", "Proxy Random Port");

    private final ConfigurationDescriptionInteger port = new ConfigurationDescriptionInteger(9999, "ProxyPort", "Proxy Port");

    @Inject
    public ProxyCoreConfigImpl(
            final Logger logger,
            final ParseUtil parseUtil,
            final ConfigurationServiceCache configurationServiceCache) {
        super(logger, parseUtil, configurationServiceCache);
    }

    @Override
    public Collection<ConfigurationDescription> getConfigurations() {
        final Set<ConfigurationDescription> result = new HashSet<>();
        result.add(conversationHistory);
        result.add(conversationCrawler);
        result.add(randomPort);
        result.add(port);
        return result;
    }

    @Override
    public boolean conversationHistory() {
        return Boolean.TRUE.equals(getValueBoolean(conversationHistory));
    }

    @Override
    public boolean conversationCrawler() {
        return Boolean.TRUE.equals(getValueBoolean(conversationCrawler));
    }

    @Override
    public boolean randomPort() {
        return Boolean.TRUE.equals(getValueBoolean(randomPort));
    }

    @Override
    public Integer getPort() {
        return getValueInteger(port);
    }

}
