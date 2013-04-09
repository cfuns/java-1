package de.benjaminborbe.notification.config;

import java.util.Collection;

import de.benjaminborbe.configuration.api.ConfigurationDescription;

public interface NotificationConfig {

	Collection<ConfigurationDescription> getConfigurations();

	String getEmailFrom();

}
