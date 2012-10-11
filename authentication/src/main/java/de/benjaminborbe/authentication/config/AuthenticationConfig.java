package de.benjaminborbe.authentication.config;

import java.util.Collection;

import de.benjaminborbe.configuration.api.ConfigurationDescription;

public interface AuthenticationConfig {

	String getProviderUrl();

	String getDomain();

	String getCredentials();

	Collection<ConfigurationDescription> getConfigurations();

}
