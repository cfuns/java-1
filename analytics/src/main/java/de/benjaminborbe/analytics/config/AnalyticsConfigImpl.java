package de.benjaminborbe.analytics.config;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.analytics.AnalyticsConstants;
import de.benjaminborbe.configuration.api.ConfigurationDescription;
import de.benjaminborbe.configuration.api.ConfigurationService;
import de.benjaminborbe.configuration.tools.ConfigurationBase;
import de.benjaminborbe.configuration.tools.ConfigurationDescriptionBoolean;
import de.benjaminborbe.tools.util.ParseUtil;

@Singleton
public class AnalyticsConfigImpl extends ConfigurationBase implements AnalyticsConfig {

	private final ConfigurationDescriptionBoolean cronActive = new ConfigurationDescriptionBoolean(false, AnalyticsConstants.CONFIG_CRON_ACTIVE, "Analytics Cron Active");

	private final ConfigurationDescriptionBoolean deleteLog = new ConfigurationDescriptionBoolean(false, AnalyticsConstants.CONFIG_DELETE_LOG, "Analytics Delete Log");

	@Inject
	public AnalyticsConfigImpl(final Logger logger, final ConfigurationService configurationService, final ParseUtil parseUtil) {
		super(logger, configurationService, parseUtil);
	}

	@Override
	public Collection<ConfigurationDescription> getConfigurations() {
		final Set<ConfigurationDescription> result = new HashSet<ConfigurationDescription>();
		result.add(cronActive);
		result.add(deleteLog);
		return result;
	}

	@Override
	public Boolean getCronActive() {
		return getValueBoolean(cronActive);
	}

	@Override
	public Boolean getDeleteLog() {
		return getValueBoolean(deleteLog);
	}

}
