package de.benjaminborbe.confluence.config;

import de.benjaminborbe.configuration.api.ConfigurationDescription;
import de.benjaminborbe.configuration.api.ConfigurationService;
import de.benjaminborbe.configuration.tools.ConfigurationBase;
import de.benjaminborbe.configuration.tools.ConfigurationDescriptionBoolean;
import de.benjaminborbe.configuration.tools.ConfigurationDescriptionInteger;
import de.benjaminborbe.tools.util.ParseUtil;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class ConfluenceConfigImpl extends ConfigurationBase implements ConfluenceConfig {

	private final ConfigurationDescriptionInteger refreshLimit = new ConfigurationDescriptionInteger(-1, "ConfluenceRefreshLimit", "Confluence Refresh Limit");

	private final ConfigurationDescriptionBoolean cronEnabled = new ConfigurationDescriptionBoolean(false, "ConfluenceCronEnabled", "Confluence Cron Enabled");

	@Inject
	public ConfluenceConfigImpl(
		final Logger logger,
		final ConfigurationService configurationService,
		final ParseUtil parseUtil
	) {
		super(logger, parseUtil, configurationService);
	}

	@Override
	public Collection<ConfigurationDescription> getConfigurations() {
		final Set<ConfigurationDescription> result = new HashSet<ConfigurationDescription>();
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
