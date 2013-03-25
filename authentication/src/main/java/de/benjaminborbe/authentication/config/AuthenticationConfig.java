package de.benjaminborbe.authentication.config;

import java.util.Collection;

import de.benjaminborbe.configuration.api.ConfigurationDescription;

public interface AuthenticationConfig {

	String getEmailFrom();

	String getProviderUrl();

	String getDomain();

	String getCredentials();

	boolean isSSL();

	Collection<ConfigurationDescription> getConfigurations();

	boolean isLdapEnabled();

}
