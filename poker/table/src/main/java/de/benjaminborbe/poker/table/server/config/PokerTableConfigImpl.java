package de.benjaminborbe.poker.table.server.config;

import de.benjaminborbe.configuration.api.ConfigurationDescription;
import de.benjaminborbe.configuration.api.ConfigurationService;
import de.benjaminborbe.configuration.tools.ConfigurationBase;
import de.benjaminborbe.configuration.tools.ConfigurationDescriptionString;
import de.benjaminborbe.tools.util.ParseUtil;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class PokerTableConfigImpl extends ConfigurationBase implements PokerTableConfig {

	private final ConfigurationDescriptionString dashboardToken = new ConfigurationDescriptionString("P2huWY8zZWDd", "PokerJsonApiDashboardToken", "Poker Json Api Dashboard Token");

	@Inject
	public PokerTableConfigImpl(
		final Logger logger,
		final ConfigurationService configurationService,
		final ParseUtil parseUtil
	) {
		super(logger, parseUtil, configurationService);
	}

	@Override
	public Collection<ConfigurationDescription> getConfigurations() {
		final Set<ConfigurationDescription> result = new HashSet<ConfigurationDescription>();
		return result;
	}

	@Override
	public String getJsonApiDashboardToken() {
		return getValueString(dashboardToken);
	}

}
