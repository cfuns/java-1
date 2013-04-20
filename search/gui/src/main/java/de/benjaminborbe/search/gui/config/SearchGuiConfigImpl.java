package de.benjaminborbe.search.gui.config;

import javax.inject.Inject;
import de.benjaminborbe.configuration.api.ConfigurationDescription;
import de.benjaminborbe.configuration.api.ConfigurationService;
import de.benjaminborbe.configuration.tools.ConfigurationBase;
import de.benjaminborbe.configuration.tools.ConfigurationDescriptionString;
import de.benjaminborbe.search.gui.SearchGuiConstants;
import de.benjaminborbe.tools.util.ParseUtil;
import org.slf4j.Logger;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class SearchGuiConfigImpl extends ConfigurationBase implements SearchGuiConfig {

	private final ConfigurationDescriptionString authToken = new ConfigurationDescriptionString(null, SearchGuiConstants.CONFIG_AUTH_TOKEN, "Search Auth Token");

	@Inject
	public SearchGuiConfigImpl(final Logger logger, final ConfigurationService configurationService, final ParseUtil parseUtil) {
		super(logger, configurationService, parseUtil);
	}

	@Override
	public Collection<ConfigurationDescription> getConfigurations() {
		final Set<ConfigurationDescription> result = new HashSet<>();
		result.add(authToken);
		return result;
	}

	@Override
	public String getAuthToken() {
		return getValueString(authToken);
	}

}
