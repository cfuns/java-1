package de.benjaminborbe.lunch.config;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;

import com.google.inject.Inject;

import de.benjaminborbe.configuration.api.ConfigurationDescription;
import de.benjaminborbe.configuration.api.ConfigurationService;
import de.benjaminborbe.configuration.api.ConfigurationServiceException;
import de.benjaminborbe.configuration.api.ConfigurationDescriptionString;

public class LunchConfigImpl implements LunchConfig {

	private final ConfigurationDescriptionString confluenceUsername = new ConfigurationDescriptionString("username", "LunchConfluenceUsername", "Lunch Username for Confluence");

	private final ConfigurationDescriptionString confluencePassword = new ConfigurationDescriptionString("password", "LunchConfluencePassword", "Lunch Password for Confluence");

	private final ConfigurationService configurationService;

	private final Logger logger;

	@Inject
	public LunchConfigImpl(final Logger logger, final ConfigurationService configurationService) {
		this.logger = logger;
		this.configurationService = configurationService;
	}

	@Override
	public String getConfluenceUsername() {
		return getValue(confluenceUsername);
	}

	@Override
	public String getConfluencePassword() {
		return getValue(confluencePassword);
	}

	private String getValue(final ConfigurationDescription configuration) {
		try {
			return configurationService.getConfigurationValue(configuration);
		}
		catch (final ConfigurationServiceException e) {
			logger.trace("ConfigurationServiceException", e);
			return configuration.getDefaultValueAsString();
		}
	}

	@Override
	public Collection<ConfigurationDescription> getConfigurations() {
		final Set<ConfigurationDescription> result = new HashSet<ConfigurationDescription>();
		result.add(confluenceUsername);
		result.add(confluencePassword);
		return result;
	}

}
