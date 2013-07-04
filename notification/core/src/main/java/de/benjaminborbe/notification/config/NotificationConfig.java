package de.benjaminborbe.notification.config;

import de.benjaminborbe.configuration.api.ConfigurationDescription;

import java.util.Collection;

public interface NotificationConfig {

	Collection<ConfigurationDescription> getConfigurations();

	String getEmailFrom();

}
