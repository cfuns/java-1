package de.benjaminborbe.configuration.tools;

import java.util.Collection;

import org.slf4j.Logger;

import de.benjaminborbe.configuration.api.ConfigurationDescription;
import de.benjaminborbe.configuration.api.ConfigurationService;
import de.benjaminborbe.configuration.api.ConfigurationServiceException;
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

	protected String getValueString(final ConfigurationDescription configuration) {
		try {
			return configurationService.getConfigurationValue(configuration);
		}
		catch (final ConfigurationServiceException e) {
			logger.debug(e.getClass().getName(), e);
			return configuration.getDefaultValueAsString();
		}
	}

	protected Long getValueLong(final ConfigurationDescriptionLong configuration) {
		try {
			return parseUtil.parseLong(configurationService.getConfigurationValue(configuration));
		}
		catch (final ConfigurationServiceException e) {
			logger.debug(e.getClass().getName(), e);
			return configuration.getDefaultValue();
		}
		catch (final ParseException e) {
			logger.trace(e.getClass().getName(), e);
			return configuration.getDefaultValue();
		}
	}

	protected Integer getValueInteger(final ConfigurationDescriptionInteger configuration) {
		try {
			return parseUtil.parseInt(configurationService.getConfigurationValue(configuration));
		}
		catch (final ConfigurationServiceException e) {
			logger.debug(e.getClass().getName(), e);
			return configuration.getDefaultValue();
		}
		catch (final ParseException e) {
			logger.trace(e.getClass().getName(), e);
			return configuration.getDefaultValue();
		}
	}

	protected Boolean getValueBoolean(final ConfigurationDescriptionBoolean configuration) {
		try {
			return parseUtil.parseBoolean(configurationService.getConfigurationValue(configuration));
		}
		catch (final ConfigurationServiceException e) {
			logger.debug(e.getClass().getName(), e);
			return configuration.getDefaultValue();
		}
		catch (final ParseException e) {
			logger.trace(e.getClass().getName(), e);
			return configuration.getDefaultValue();
		}
	}

	public abstract Collection<ConfigurationDescription> getConfigurations();

}
