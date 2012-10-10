package de.benjaminborbe.configuration.api;

import java.util.Collection;
import org.slf4j.Logger;

import de.benjaminborbe.tools.util.ParseException;
import de.benjaminborbe.tools.util.ParseUtil;

public abstract class ConfigurationBase {

	private final ConfigurationService configurationService;

	private final Logger logger;

	private final ParseUtil parseUtil;

	public ConfigurationBase(final Logger logger, final ConfigurationService configurationService, final ParseUtil parseUtil) {
		this.logger = logger;
		this.configurationService = configurationService;
		this.parseUtil = parseUtil;
	}

	protected String getValueString(final ConfigurationDescriptionString configuration) {
		try {
			return configurationService.getConfigurationValue(configuration);
		}
		catch (final ConfigurationServiceException e) {
			logger.trace(e.getClass().getName(), e);
			return configuration.getDefaultValueAsString();
		}
	}

	protected int getValueInt(final ConfigurationDescriptionInt configuration) {
		try {
			return parseUtil.parseInt(configurationService.getConfigurationValue(configuration));
		}
		catch (final ConfigurationServiceException e) {
			logger.trace(e.getClass().getName(), e);
			return configuration.getDefaultValue();
		}
		catch (final ParseException e) {
			logger.trace(e.getClass().getName(), e);
			return configuration.getDefaultValue();
		}
	}

	public abstract Collection<ConfigurationDescription> getConfigurations();

}
