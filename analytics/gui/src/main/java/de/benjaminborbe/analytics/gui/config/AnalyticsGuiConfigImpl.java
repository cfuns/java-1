package de.benjaminborbe.analytics.gui.config;

import de.benjaminborbe.analytics.gui.AnalyticsGuiConstants;
import de.benjaminborbe.configuration.api.ConfigurationDescription;
import de.benjaminborbe.configuration.api.ConfigurationService;
import de.benjaminborbe.configuration.tools.ConfigurationBase;
import de.benjaminborbe.configuration.tools.ConfigurationDescriptionString;
import de.benjaminborbe.configuration.tools.ConfigurationServiceCache;
import de.benjaminborbe.tools.util.ParseUtil;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class AnalyticsGuiConfigImpl extends ConfigurationBase implements AnalyticsGuiConfig {

	private final ConfigurationDescriptionString authToken = new ConfigurationDescriptionString(null, AnalyticsGuiConstants.CONFIG_AUTH_TOKEN, "Analytics Auth Token");

	@Inject
	public AnalyticsGuiConfigImpl(
		final Logger logger,
		final ConfigurationService configurationService,
		final ParseUtil parseUtil,
		final ConfigurationServiceCache configurationServiceCache
	) {
		super(logger, configurationService, parseUtil, configurationServiceCache);
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
