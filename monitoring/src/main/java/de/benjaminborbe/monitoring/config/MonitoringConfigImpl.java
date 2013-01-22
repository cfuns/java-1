package de.benjaminborbe.monitoring.config;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;

import com.google.inject.Inject;

import de.benjaminborbe.configuration.api.ConfigurationDescription;
import de.benjaminborbe.configuration.api.ConfigurationService;
import de.benjaminborbe.configuration.tools.ConfigurationBase;
import de.benjaminborbe.configuration.tools.ConfigurationDescriptionBoolean;
import de.benjaminborbe.tools.util.ParseUtil;

public class MonitoringConfigImpl extends ConfigurationBase implements MonitoringConfig {

	private final ConfigurationDescriptionBoolean cronEnabled = new ConfigurationDescriptionBoolean(false, "MonitoringCronEnabled", "Monitoring Cron Enabled");

	@Inject
	public MonitoringConfigImpl(final Logger logger, final ConfigurationService configurationService, final ParseUtil parseUtil) {
		super(logger, configurationService, parseUtil);
	}

	@Override
	public Collection<ConfigurationDescription> getConfigurations() {
		final Set<ConfigurationDescription> result = new HashSet<ConfigurationDescription>();
		result.add(cronEnabled);
		return result;
	}

	@Override
	public boolean isCronEnabled() {
		return getValueBoolean(cronEnabled);
	}

}
