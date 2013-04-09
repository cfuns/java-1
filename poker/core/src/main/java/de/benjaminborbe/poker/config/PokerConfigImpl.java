package de.benjaminborbe.poker.config;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;

import com.google.inject.Inject;

import de.benjaminborbe.configuration.api.ConfigurationDescription;
import de.benjaminborbe.configuration.api.ConfigurationService;
import de.benjaminborbe.configuration.tools.ConfigurationBase;
import de.benjaminborbe.configuration.tools.ConfigurationDescriptionBoolean;
import de.benjaminborbe.configuration.tools.ConfigurationDescriptionLong;
import de.benjaminborbe.tools.util.ParseUtil;

public class PokerConfigImpl extends ConfigurationBase implements PokerConfig {

	private final ConfigurationDescriptionBoolean cronEnabled = new ConfigurationDescriptionBoolean(false, "PokerCronEnabled", "Poker Cron Enabled");

	private final ConfigurationDescriptionBoolean creditsNegativeAllowed = new ConfigurationDescriptionBoolean(false, "PokerCreditsNegativeAllowed", "Poker Credits Negative Allowed");

	private final ConfigurationDescriptionLong autoFoldTimeout = new ConfigurationDescriptionLong(0l, "PokerAutoFoldTimeout", "Poker Auto Fold Timeout");

	private final ConfigurationDescriptionLong maxBid = new ConfigurationDescriptionLong(100000l, "PokerMaxBid", "Poker Max Bid");

	@Inject
	public PokerConfigImpl(final Logger logger, final ConfigurationService configurationService, final ParseUtil parseUtil) {
		super(logger, configurationService, parseUtil);
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

}
