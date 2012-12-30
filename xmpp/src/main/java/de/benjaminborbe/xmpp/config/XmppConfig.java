package de.benjaminborbe.xmpp.config;

import java.util.Collection;

import de.benjaminborbe.configuration.api.ConfigurationDescription;

public interface XmppConfig {

	Collection<ConfigurationDescription> getConfigurations();

	String getUsername();

	String getPassword();

	String getServerHost();

	Integer getServerPort();
}
