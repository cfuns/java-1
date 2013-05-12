package de.benjaminborbe.selenium.core.config;

import de.benjaminborbe.configuration.api.ConfigurationDescription;

import java.util.Collection;

public interface SeleniumCoreConfig {

	Collection<ConfigurationDescription> getConfigurations();

	String getSeleniumRemoteHost();

	Integer getSeleniumRemotePort();

}
