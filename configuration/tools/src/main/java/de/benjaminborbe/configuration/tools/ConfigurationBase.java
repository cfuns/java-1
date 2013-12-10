package de.benjaminborbe.configuration.tools;

import de.benjaminborbe.configuration.api.ConfigurationDescription;
import de.benjaminborbe.configuration.api.ConfigurationIdentifier;
import de.benjaminborbe.configuration.api.ConfigurationService;
import de.benjaminborbe.configuration.api.ConfigurationServiceException;
import de.benjaminborbe.tools.util.ParseException;
import de.benjaminborbe.tools.util.ParseUtil;
import org.slf4j.Logger;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public abstract class ConfigurationBase {

	private final Logger logger;

	private final ParseUtil parseUtil;

	private final ConfigurationService configurationService;

	private final Map<ConfigurationIdentifier, String> cache = new HashMap<ConfigurationIdentifier, String>();

	public ConfigurationBase(
		final Logger logger,
		final ParseUtil parseUtil,
		final ConfigurationService configurationService
	) {
		this.logger = logger;
		this.parseUtil = parseUtil;
		this.configurationService = configurationService;
	}

	private String getConfigurationValue(final ConfigurationDescription configurationDescription) throws ConfigurationServiceException {
		if (isCacheEnabled()) {
			final ConfigurationIdentifier id = configurationDescription.getId();
			if (cache.containsKey(id)) {
				return cache.get(id);
			} else {
				final String configurationValue = configurationService.getConfigurationValue(configurationDescription);
				cache.put(id, configurationValue);
				return configurationValue;
			}
		} else {
			return configurationService.getConfigurationValue(configurationDescription);
		}
	}

	protected boolean isCacheEnabled() {
		return false;
	}

	protected String getValueString(final ConfigurationDescription configurationDescription) {
		try {
			return getConfigurationValue(configurationDescription);
		} catch (final ConfigurationServiceException e) {
			logger.debug(e.getClass().getName(), e);
			return configurationDescription.getDefaultValueAsString();
		}
	}

	protected Double getValueDouble(final ConfigurationDescriptionDouble configurationDescriptionDouble) {
		try {
			return parseUtil.parseDouble(getConfigurationValue(configurationDescriptionDouble));
		} catch (final ConfigurationServiceException e) {
			logger.debug(e.getClass().getName(), e);
			return configurationDescriptionDouble.getDefaultValue();
		} catch (final ParseException e) {
			logger.trace(e.getClass().getName(), e);
			return configurationDescriptionDouble.getDefaultValue();
		}
	}

	protected Float getValueFloat(final ConfigurationDescriptionFloat configurationDescriptionFloat) {
		try {
			return parseUtil.parseFloat(getConfigurationValue(configurationDescriptionFloat));
		} catch (final ConfigurationServiceException e) {
			logger.debug(e.getClass().getName(), e);
			return configurationDescriptionFloat.getDefaultValue();
		} catch (final ParseException e) {
			logger.trace(e.getClass().getName(), e);
			return configurationDescriptionFloat.getDefaultValue();
		}
	}

	protected Long getValueLong(final ConfigurationDescriptionLong configurationDescriptionLong) {
		try {
			return parseUtil.parseLong(getConfigurationValue(configurationDescriptionLong));
		} catch (final ConfigurationServiceException e) {
			logger.debug(e.getClass().getName(), e);
			return configurationDescriptionLong.getDefaultValue();
		} catch (final ParseException e) {
			logger.trace(e.getClass().getName(), e);
			return configurationDescriptionLong.getDefaultValue();
		}
	}

	protected Integer getValueInteger(final ConfigurationDescriptionInteger configurationDescriptionInteger) {
		try {
			return parseUtil.parseInt(getConfigurationValue(configurationDescriptionInteger));
		} catch (final ConfigurationServiceException e) {
			logger.debug(e.getClass().getName(), e);
			return configurationDescriptionInteger.getDefaultValue();
		} catch (final ParseException e) {
			logger.trace(e.getClass().getName(), e);
			return configurationDescriptionInteger.getDefaultValue();
		}
	}

	protected Boolean getValueBoolean(final ConfigurationDescriptionBoolean configurationDescriptionBoolean) {
		try {
			return parseUtil.parseBoolean(getConfigurationValue(configurationDescriptionBoolean));
		} catch (final ConfigurationServiceException e) {
			logger.debug(e.getClass().getName(), e);
			return configurationDescriptionBoolean.getDefaultValue();
		} catch (final ParseException e) {
			logger.trace(e.getClass().getName(), e);
			return configurationDescriptionBoolean.getDefaultValue();
		}
	}

	public abstract Collection<ConfigurationDescription> getConfigurations();

}
