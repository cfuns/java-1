package de.benjaminborbe.authentication.gui.config;

import de.benjaminborbe.authentication.gui.AuthenticationGuiConstants;
import de.benjaminborbe.configuration.api.ConfigurationDescription;
import de.benjaminborbe.configuration.api.ConfigurationService;
import de.benjaminborbe.configuration.tools.ConfigurationBase;
import de.benjaminborbe.configuration.tools.ConfigurationDescriptionBoolean;
import de.benjaminborbe.configuration.tools.ConfigurationServiceCache;
import de.benjaminborbe.tools.util.ParseUtil;
import org.slf4j.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Singleton
public class AuthenticationGuiConfigImpl extends ConfigurationBase implements AuthenticationGuiConfig {

	private final ConfigurationDescriptionBoolean registerEnabled = new ConfigurationDescriptionBoolean(true, AuthenticationGuiConstants.CONFIG_SERVERPORT,
		"Authentication register enabled");

	@Inject
	public AuthenticationGuiConfigImpl(
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
		result.add(registerEnabled);
		return result;
	}

	@Override
	public boolean registerEnabled() {
		return getValueBoolean(registerEnabled);
	}
}
