package de.benjaminborbe.microblog.config;

import java.util.Collection;

import de.benjaminborbe.configuration.api.ConfigurationDescription;

public interface MicroblogConfig {

	boolean isCronEnabled();

	boolean isMailEnabled();

	Collection<ConfigurationDescription> getConfigurations();

	String getMicroblogRssFeed();

	String getMicroblogUrl();
}
