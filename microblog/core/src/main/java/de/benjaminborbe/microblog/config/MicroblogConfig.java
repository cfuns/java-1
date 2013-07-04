package de.benjaminborbe.microblog.config;

import de.benjaminborbe.configuration.api.ConfigurationDescription;

import java.util.Collection;

public interface MicroblogConfig {

	boolean isCronEnabled();

	boolean isMailEnabled();

	Collection<ConfigurationDescription> getConfigurations();

	String getMicroblogRssFeed();

	String getMicroblogUrl();
}
