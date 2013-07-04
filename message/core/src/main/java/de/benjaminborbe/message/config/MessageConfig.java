package de.benjaminborbe.message.config;

import de.benjaminborbe.configuration.api.ConfigurationDescription;

import java.util.Collection;

public interface MessageConfig {

	Integer getConsumerAmount();

	Collection<ConfigurationDescription> getConfigurations();
}
