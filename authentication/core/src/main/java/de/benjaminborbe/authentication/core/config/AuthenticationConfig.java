package de.benjaminborbe.authentication.core.config;

import de.benjaminborbe.configuration.api.ConfigurationDescription;

import java.util.Collection;

public interface AuthenticationConfig {

	String getEmailFrom();

	String getProviderUrl();

	String getDomain();

	String getCredentials();

	boolean isSSL();

	Collection<ConfigurationDescription> getConfigurations();

	boolean isLdapEnabled();

}
