package de.benjaminborbe.lunch.config;

import de.benjaminborbe.configuration.api.ConfigurationDescription;
import de.benjaminborbe.configuration.api.ConfigurationService;
import de.benjaminborbe.configuration.tools.ConfigurationBase;
import de.benjaminborbe.configuration.tools.ConfigurationDescriptionString;
import de.benjaminborbe.tools.util.ParseUtil;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class LunchConfigImpl extends ConfigurationBase implements LunchConfig {

	private final ConfigurationDescriptionString confluenceSpaceKey = new ConfigurationDescriptionString("MITTAG", "LunchConfluenceSpaceKey", "Lunch SpaceKey for Confluence");

	private final ConfigurationDescriptionString confluenceUsername = new ConfigurationDescriptionString(null, "LunchConfluenceUsername", "Lunch Username for Confluence");

	private final ConfigurationDescriptionString confluencePassword = new ConfigurationDescriptionString(null, "LunchConfluencePassword", "Lunch Password for Confluence");

	private final ConfigurationDescriptionString mittagNotifyKeywords = new ConfigurationDescriptionString("Essen, Mittagessen, Mittagstisch", "LunchNotifyKeywords",
		"Lunch Notify Keywords");

	@Inject
	public LunchConfigImpl(
		final Logger logger,
		final ConfigurationService configurationService,
		final ParseUtil parseUtil
	) {
		super(logger, parseUtil, configurationService);
	}

	@Override
	public String getConfluenceUsername() {
		return getValueString(confluenceUsername);
	}

	@Override
	public String getConfluencePassword() {
		return getValueString(confluencePassword);
	}

	@Override
	public Collection<ConfigurationDescription> getConfigurations() {
		final Set<ConfigurationDescription> result = new HashSet<ConfigurationDescription>();
		result.add(confluenceSpaceKey);
		result.add(confluenceUsername);
		result.add(confluencePassword);
		result.add(mittagNotifyKeywords);
		return result;
	}

	@Override
	public String getConfluenceSpaceKey() {
		return getValueString(confluenceSpaceKey);
	}

	@Override
	public List<String> getMittagNotifyKeywords() {
		final String value = getValueString(mittagNotifyKeywords);
		final List<String> result = new ArrayList<String>();
		if (value != null) {
			for (final String part : value.split(",")) {
				result.add(part.trim());
			}
		}
		return result;
	}
}
