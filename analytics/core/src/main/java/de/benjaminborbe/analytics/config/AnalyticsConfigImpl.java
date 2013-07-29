package de.benjaminborbe.analytics.config;

import de.benjaminborbe.analytics.AnalyticsConstants;
import de.benjaminborbe.configuration.api.ConfigurationDescription;
import de.benjaminborbe.configuration.api.ConfigurationService;
import de.benjaminborbe.configuration.tools.ConfigurationBase;
import de.benjaminborbe.configuration.tools.ConfigurationDescriptionBoolean;
import de.benjaminborbe.configuration.tools.ConfigurationDescriptionLong;
import de.benjaminborbe.configuration.tools.ConfigurationServiceCache;
import de.benjaminborbe.tools.util.ParseUtil;
import org.slf4j.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Singleton
public class AnalyticsConfigImpl extends ConfigurationBase implements AnalyticsConfig {

	private final ConfigurationDescriptionBoolean cronActive = new ConfigurationDescriptionBoolean(false, AnalyticsConstants.CONFIG_CRON_ACTIVE, "Analytics Cron Active");

	private final ConfigurationDescriptionLong aggregationChunkSize = new ConfigurationDescriptionLong(100l, AnalyticsConstants.CONFIG_AGGREGATION_CHUNK_SIZE,
		"Analytics Aggregation Chunk Size");

	@Inject
	public AnalyticsConfigImpl(
		final Logger logger,
		final ConfigurationService configurationService,
		final ParseUtil parseUtil,
		final ConfigurationServiceCache configurationServiceCache
	) {
		super(logger, parseUtil, configurationServiceCache);
	}

	@Override
	public Collection<ConfigurationDescription> getConfigurations() {
		final Set<ConfigurationDescription> result = new HashSet<ConfigurationDescription>();
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
