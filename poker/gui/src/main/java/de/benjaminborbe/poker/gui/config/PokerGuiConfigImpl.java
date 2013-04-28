package de.benjaminborbe.poker.gui.config;

import de.benjaminborbe.configuration.api.ConfigurationDescription;
import de.benjaminborbe.configuration.api.ConfigurationService;
import de.benjaminborbe.configuration.tools.ConfigurationBase;
import de.benjaminborbe.configuration.tools.ConfigurationDescriptionBoolean;
import de.benjaminborbe.configuration.tools.ConfigurationServiceCache;
import de.benjaminborbe.tools.util.ParseUtil;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class PokerGuiConfigImpl extends ConfigurationBase implements PokerGuiConfig {

	private final ConfigurationDescriptionBoolean jsonApiEnabled = new ConfigurationDescriptionBoolean(false, "PokerJsonApiEnabled", "Poker Json Api Enabled");

	@Inject
	public PokerGuiConfigImpl(
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
		result.add(jsonApiEnabled);
		return result;
	}

	@Override
	public boolean isJsonApiEnabled() {
		return Boolean.TRUE.equals(getValueBoolean(jsonApiEnabled));
	}

}
