package de.benjaminborbe.search.config;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;

import com.google.inject.Inject;

import de.benjaminborbe.configuration.api.ConfigurationDescription;
import de.benjaminborbe.configuration.api.ConfigurationService;
import de.benjaminborbe.configuration.tools.ConfigurationBase;
import de.benjaminborbe.configuration.tools.ConfigurationDescriptionBoolean;
import de.benjaminborbe.search.SearchConstants;
import de.benjaminborbe.tools.util.ParseUtil;

public class SearchConfigImpl extends ConfigurationBase implements SearchConfig {

	private final ConfigurationDescriptionBoolean isUrlSearchActive = new ConfigurationDescriptionBoolean(false, SearchConstants.CONFIG_URL_SEARCH_ACTIVE, "Search Url Active");

	@Inject
	public SearchConfigImpl(final Logger logger, final ConfigurationService configurationService, final ParseUtil parseUtil) {
		super(logger, configurationService, parseUtil);
	}

	@Override
	public Collection<ConfigurationDescription> getConfigurations() {
		final Set<ConfigurationDescription> result = new HashSet<ConfigurationDescription>();
		result.add(isUrlSearchActive);
		return result;
	}

	@Override
	public boolean isUrlSearchActive() {
		return getValueBoolean(isUrlSearchActive);
	}
}
