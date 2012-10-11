package de.benjaminborbe.lunch.config;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;

import com.google.inject.Inject;

import de.benjaminborbe.configuration.api.ConfigurationDescription;
import de.benjaminborbe.configuration.api.ConfigurationService;
import de.benjaminborbe.configuration.tools.ConfigurationBase;
import de.benjaminborbe.configuration.tools.ConfigurationDescriptionString;
import de.benjaminborbe.tools.util.ParseUtil;

public class LunchConfigImpl extends ConfigurationBase implements LunchConfig {

	private final ConfigurationDescriptionString confluenceSpaceKey = new ConfigurationDescriptionString("MITTAG", "LunchConfluenceSpaceKey", "Lunch SpaceKey for Confluence");

	private final ConfigurationDescriptionString confluenceUsername = new ConfigurationDescriptionString("username", "LunchConfluenceUsername", "Lunch Username for Confluence");

	private final ConfigurationDescriptionString confluencePassword = new ConfigurationDescriptionString("password", "LunchConfluencePassword", "Lunch Password for Confluence");

	@Inject
	public LunchConfigImpl(final Logger logger, final ConfigurationService configurationService, final ParseUtil parseUtil) {
		super(logger, configurationService, parseUtil);
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
		return result;
	}

	@Override
	public String getConfluenceSpaceKey() {
		return getValueString(confluenceSpaceKey);
	}

}
