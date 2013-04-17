package de.benjaminborbe.analytics.config;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import de.benjaminborbe.analytics.AnalyticsConstants;
import de.benjaminborbe.configuration.api.ConfigurationDescription;
import de.benjaminborbe.configuration.api.ConfigurationService;
import de.benjaminborbe.configuration.tools.ConfigurationBase;
import de.benjaminborbe.configuration.tools.ConfigurationDescriptionBoolean;
import de.benjaminborbe.configuration.tools.ConfigurationDescriptionLong;
import de.benjaminborbe.tools.util.ParseUtil;
import org.slf4j.Logger;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Singleton
public class AnalyticsConfigImpl extends ConfigurationBase implements AnalyticsConfig {

	private final ConfigurationDescriptionBoolean cronActive = new ConfigurationDescriptionBoolean(false, AnalyticsConstants.CONFIG_CRON_ACTIVE, "Analytics Cron Active");

	private final ConfigurationDescriptionLong aggregationChunkSize = new ConfigurationDescriptionLong(100l, AnalyticsConstants.CONFIG_AGGREGATION_CHUNK_SIZE,
		"Analytics Aggregation Chunk Size");

	@Inject
	public AnalyticsConfigImpl(final Logger logger, final ConfigurationService configurationService, final ParseUtil parseUtil) {
		super(logger, configurationService, parseUtil);
	}

	@Override
	public Collection<ConfigurationDescription> getConfigurations() {
		final Set<ConfigurationDescription> result = new HashSet<>();
		result.add(cronActive);
		result.add(aggregationChunkSize);
		return result;
	}

	@Override
	public Boolean getCronActive() {
		return getValueBoolean(cronActive);
	}

	@Override
	public long getAggregationChunkSize() {
		return getValueLong(aggregationChunkSize);
	}

}
