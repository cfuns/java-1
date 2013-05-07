package de.benjaminborbe.websearch.core.config;

import java.util.Collection;

import de.benjaminborbe.configuration.api.ConfigurationDescription;

public interface WebsearchConfig {

    Collection<ConfigurationDescription> getConfigurations();

    Integer getRefreshLimit();

    boolean getCronEnabled();

    boolean saveImages();
}
