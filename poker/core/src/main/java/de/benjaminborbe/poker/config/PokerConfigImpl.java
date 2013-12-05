package de.benjaminborbe.poker.config;

import de.benjaminborbe.configuration.api.ConfigurationDescription;
import de.benjaminborbe.configuration.api.ConfigurationService;
import de.benjaminborbe.configuration.tools.ConfigurationBase;
import de.benjaminborbe.configuration.tools.ConfigurationDescriptionBoolean;
import de.benjaminborbe.configuration.tools.ConfigurationDescriptionDouble;
import de.benjaminborbe.configuration.tools.ConfigurationDescriptionLong;
import de.benjaminborbe.tools.util.ParseUtil;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class PokerConfigImpl extends ConfigurationBase implements PokerConfig {

	private final ConfigurationDescriptionBoolean cronEnabled = new ConfigurationDescriptionBoolean(false, "PokerCronEnabled", "Poker Cron Enabled");

	private final ConfigurationDescriptionBoolean creditsNegativeAllowed = new ConfigurationDescriptionBoolean(false, "PokerCreditsNegativeAllowed", "Poker Credits Negative Allowed");

	private final ConfigurationDescriptionLong autoFoldTimeout = new ConfigurationDescriptionLong(0l, "PokerAutoFoldTimeout", "Poker Auto Fold Timeout in ms");

	private final ConfigurationDescriptionLong maxBid = new ConfigurationDescriptionLong(100000l, "PokerMaxBid", "Poker Max Bid");

	private final ConfigurationDescriptionDouble minRaiseFactor = new ConfigurationDescriptionDouble(2d, "PokerMinRaiseFactor", "Poker Min Raise Factor");

	private final ConfigurationDescriptionDouble maxRaiseFactor = new ConfigurationDescriptionDouble(10d, "PokerMaxRaiseFactor", "Poker Max Raise Factor");

	@Inject
	public PokerConfigImpl(
		final Logger logger,
		final ParseUtil parseUtil,
		final ConfigurationService configurationService
	) {
		super(logger, parseUtil, configurationService);
	}

	@Override
	public Collection<ConfigurationDescription> getConfigurations() {
		final Set<ConfigurationDescription> result = new HashSet<ConfigurationDescription>();
		result.add(cronEnabled);
		result.add(autoFoldTimeout);
		result.add(maxBid);
		result.add(creditsNegativeAllowed);
		return result;
	}

	@Override
	public boolean isCronEnabled() {
		return Boolean.TRUE.equals(getValueBoolean(cronEnabled));
	}

	@Override
	public long getAutoFoldTimeout() {
		return getValueLong(autoFoldTimeout);
	}

	@Override
	public long getMaxBid() {
		return getValueLong(maxBid);
	}

	@Override
	public boolean isCreditsNegativeAllowed() {
		return Boolean.TRUE.equals(getValueBoolean(creditsNegativeAllowed));
	}

	@Override
	public double getMinRaiseFactor() {
		return getValueDouble(minRaiseFactor);
	}

	@Override
	public double getMaxRaiseFactor() {
		return getValueDouble(maxRaiseFactor);
	}

}
