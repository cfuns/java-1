package de.benjaminborbe.selenium.core.config;

import de.benjaminborbe.configuration.api.ConfigurationDescription;
import de.benjaminborbe.configuration.tools.ConfigurationBase;
import de.benjaminborbe.configuration.tools.ConfigurationDescriptionInteger;
import de.benjaminborbe.configuration.tools.ConfigurationDescriptionString;
import de.benjaminborbe.configuration.tools.ConfigurationServiceCache;
import de.benjaminborbe.tools.util.ParseUtil;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class SeleniumCoreConfigImpl extends ConfigurationBase implements SeleniumCoreConfig {

	private final ConfigurationDescriptionString remoteHost = new ConfigurationDescriptionString(null, "SeleniumRemoteHost", "Selenium Remote Host");

	private final ConfigurationDescriptionInteger remotePort = new ConfigurationDescriptionInteger(null, "SeleniumRemotePort", "Selenium Remote Port");

	@Inject
	public SeleniumCoreConfigImpl(
		final Logger logger,
		final ParseUtil parseUtil,
		final ConfigurationServiceCache configurationServiceCache
	) {
		super(logger, parseUtil, configurationServiceCache);
	}

	@Override
	public Collection<ConfigurationDescription> getConfigurations() {
		final Set<ConfigurationDescription> result = new HashSet<>();
		result.add(remoteHost);
		result.add(remotePort);
		return result;
	}

	@Override
	public String getSeleniumRemoteHost() {
		return getValueString(remoteHost);
	}

	@Override
	public Integer getSeleniumRemotePort() {
		return getValueInteger(remotePort);
	}

}
