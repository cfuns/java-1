package de.benjaminborbe.kiosk.config;

import de.benjaminborbe.configuration.api.ConfigurationDescription;

import java.util.Collection;

public interface KioskConfig {

	boolean isKioskConnectorEnabled();

	Collection<ConfigurationDescription> getConfigurations();

}
