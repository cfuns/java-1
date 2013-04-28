package de.benjaminborbe.websearch.core.config;

import de.benjaminborbe.configuration.api.ConfigurationDescription;
import de.benjaminborbe.configuration.api.ConfigurationService;
import de.benjaminborbe.configuration.tools.ConfigurationBase;
import de.benjaminborbe.configuration.tools.ConfigurationDescriptionBoolean;
import de.benjaminborbe.configuration.tools.ConfigurationDescriptionInteger;
import de.benjaminborbe.configuration.tools.ConfigurationServiceCache;
import de.benjaminborbe.tools.util.ParseUtil;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class WebsearchConfigImpl extends ConfigurationBase implements WebsearchConfig {

	private final ConfigurationDescriptionInteger refreshLimit = new ConfigurationDescriptionInteger(-1, "WebsearchRefreshLimit", "Websearch Refresh Limit");

	private final ConfigurationDescriptionBoolean cronEnabled = new ConfigurationDescriptionBoolean(false, "WebsearchCronEnabled", "Websearch Cron Enabled");

	@Inject
	public WebsearchConfigImpl(
		final Logger logger,
		final ConfigurationService configurationService,
		final ParseUtil parseUtil,
		final ConfigurationServiceCache configurationServiceCache
	) {
		super(logger, parseUtil, configurationServiceCache);
	}

	@Override
	public Collection<ConfigurationDescription> getConfigurations() {
		final Set<ConfigurationDescription> result = new HashSet<>();
		result.add(refreshLimit);
		result.add(cronEnabled);
		return result;
	}

	@Override
	public Integer getRefreshLimit() {
		return getValueInteger(refreshLimit);
	}

	@Override
	public Boolean getCronEnabled() {
		return getValueBoolean(cronEnabled);
	}

}
