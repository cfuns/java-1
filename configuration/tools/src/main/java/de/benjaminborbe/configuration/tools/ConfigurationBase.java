package de.benjaminborbe.configuration.tools;

import de.benjaminborbe.configuration.api.ConfigurationDescription;
import de.benjaminborbe.configuration.api.ConfigurationServiceException;
import de.benjaminborbe.tools.util.ParseException;
import de.benjaminborbe.tools.util.ParseUtil;
import org.slf4j.Logger;

import java.util.Collection;

public abstract class ConfigurationBase {

	private final Logger logger;

	private final ParseUtil parseUtil;

	private final ConfigurationServiceCache configurationServiceCache;

	public ConfigurationBase(
		final Logger logger,
		final ParseUtil parseUtil,
		final ConfigurationServiceCache configurationServiceCache
	) {
		this.logger = logger;
		this.parseUtil = parseUtil;
		this.configurationServiceCache = configurationServiceCache;
	}

	private String getConfigurationValue(final ConfigurationDescription configurationDescription) throws ConfigurationServiceException {
		return configurationServiceCache.getConfigurationValue(configurationDescription);
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
