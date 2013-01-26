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
import de.benjaminborbe.configuration.tools.ConfigurationDescriptionString;
import de.benjaminborbe.monitoring.MonitoringConstants;
import de.benjaminborbe.tools.util.ParseUtil;

public class MonitoringConfigImpl extends ConfigurationBase implements MonitoringConfig {

	private final ConfigurationDescriptionBoolean cronEnabled = new ConfigurationDescriptionBoolean(false, MonitoringConstants.CONFIG_CRON_ENABLED, "Monitoring Cron Enabled");

	private final ConfigurationDescriptionString authToken = new ConfigurationDescriptionString(null, MonitoringConstants.CONFIG_AUTH_TOKEN, "Monitoring Auth Token");

	@Inject
	public MonitoringConfigImpl(final Logger logger, final ConfigurationService configurationService, final ParseUtil parseUtil) {
		super(logger, configurationService, parseUtil);
	}

	@Override
	public Collection<ConfigurationDescription> getConfigurations() {
		final Set<ConfigurationDescription> result = new HashSet<ConfigurationDescription>();
		result.add(cronEnabled);
		result.add(authToken);
		return result;
	}

	@Override
	public boolean isCronEnabled() {
		return getValueBoolean(cronEnabled);
	}

	@Override
	public String getAuthToken() {
		return getValueString(authToken);
	}

}
