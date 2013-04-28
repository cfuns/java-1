package de.benjaminborbe.lunch.gui.config;

import de.benjaminborbe.configuration.api.ConfigurationDescription;
import de.benjaminborbe.configuration.api.ConfigurationService;
import de.benjaminborbe.configuration.tools.ConfigurationBase;
import de.benjaminborbe.configuration.tools.ConfigurationDescriptionString;
import de.benjaminborbe.configuration.tools.ConfigurationServiceCache;
import de.benjaminborbe.lunch.gui.LunchGuiConstants;
import de.benjaminborbe.tools.util.ParseUtil;
import org.slf4j.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Singleton
public class LunchGuiConfigImpl extends ConfigurationBase implements LunchGuiConfig {

	private final ConfigurationDescriptionString authToken = new ConfigurationDescriptionString(null, LunchGuiConstants.CONFIG_AUTH_TOKEN, "Lunch Auth Token");

	@Inject
	public LunchGuiConfigImpl(
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
		result.add(authToken);
		return result;
	}

	@Override
	public String getAuthToken() {
		return getValueString(authToken);
	}

}
