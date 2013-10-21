package de.benjaminborbe.message.config;

import java.util.Collection;

import de.benjaminborbe.configuration.api.ConfigurationDescription;

public interface MessageConfig {

    Integer getConsumerAmount();

    Collection<ConfigurationDescription> getConfigurations();

    String getScheduleExpression();
}
