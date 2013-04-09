package de.benjaminborbe.kiosk.config;

import java.util.Collection;

import de.benjaminborbe.configuration.api.ConfigurationDescription;

public interface KioskConfig {

	boolean isKioskConnectorEnabled();

	Collection<ConfigurationDescription> getConfigurations();

}
