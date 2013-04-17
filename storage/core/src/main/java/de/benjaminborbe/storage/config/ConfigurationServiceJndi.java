package de.benjaminborbe.storage.config;

import com.google.inject.Inject;
import de.benjaminborbe.api.ValidationException;
import de.benjaminborbe.configuration.api.ConfigurationDescription;
import de.benjaminborbe.configuration.api.ConfigurationIdentifier;
import de.benjaminborbe.configuration.api.ConfigurationService;
import de.benjaminborbe.configuration.api.ConfigurationServiceException;
import de.benjaminborbe.tools.jndi.JndiContext;
import org.slf4j.Logger;

import javax.naming.NameNotFoundException;
import javax.naming.NamingException;
import javax.naming.NoInitialContextException;
import java.util.Collection;

public class ConfigurationServiceJndi implements ConfigurationService {

	private final JndiContext jndiContext;

	private final Logger logger;

	@Inject
	public ConfigurationServiceJndi(final Logger logger, final JndiContext jndiContext) {
		this.logger = logger;
		this.jndiContext = jndiContext;
	}

	@Override
	public String getConfigurationValue(final ConfigurationIdentifier configurationIdentifier) throws ConfigurationServiceException {
		return null;
	}

	@Override
	public String getConfigurationValue(final ConfigurationDescription configuration) throws ConfigurationServiceException {
		try {
			final String name = configuration.getId().getId();
			logger.trace("getConfigurationValue - name: " + name);
			final Object value = jndiContext.lookup(name);
			if (value != null) {
				logger.trace("getConfigurationValue - name: " + name + " value: " + value);
				return String.valueOf(value);
			}
		} catch (final NameNotFoundException | NoInitialContextException e) {
		} catch (final NamingException e) {
			logger.debug(e.getClass().getName(), e);
		}
		return configuration.getDefaultValueAsString();
	}

	@Override
	public void setConfigurationValue(final ConfigurationIdentifier configurationIdentifier, final String value) throws ConfigurationServiceException, ValidationException {
	}

	@Override
	public ConfigurationIdentifier createConfigurationIdentifier(final String id) throws ConfigurationServiceException {
		return null;
	}

	@Override
	public Collection<ConfigurationDescription> listConfigurations() throws ConfigurationServiceException {
		return null;
	}

	@Override
	public ConfigurationDescription getConfiguration(final ConfigurationIdentifier configurationIdentifier) throws ConfigurationServiceException {
		return null;
	}

}
