package de.benjaminborbe.xmpp.config;

import de.benjaminborbe.configuration.api.ConfigurationDescription;

import java.util.Collection;

public interface XmppConfig {

	Collection<ConfigurationDescription> getConfigurations();

	String getUsername();

	String getPassword();

	String getServerHost();

	Integer getServerPort();
}
