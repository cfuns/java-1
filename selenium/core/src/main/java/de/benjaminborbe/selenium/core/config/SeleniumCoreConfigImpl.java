package de.benjaminborbe.selenium.core.config;

import de.benjaminborbe.configuration.api.ConfigurationDescription;
import de.benjaminborbe.configuration.tools.ConfigurationBase;
import de.benjaminborbe.configuration.tools.ConfigurationDescriptionBoolean;
import de.benjaminborbe.configuration.tools.ConfigurationDescriptionInteger;
import de.benjaminborbe.configuration.tools.ConfigurationDescriptionString;
import de.benjaminborbe.configuration.tools.ConfigurationServiceCache;
import de.benjaminborbe.selenium.core.SeleniumCoreConstatns;
import de.benjaminborbe.tools.util.ParseUtil;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class SeleniumCoreConfigImpl extends ConfigurationBase implements SeleniumCoreConfig {

	private final ConfigurationDescriptionString remoteHost = new ConfigurationDescriptionString(null, SeleniumCoreConstatns.CONFIG_SELENIUM_REMOTE_HOST, "Selenium Remote Host");

	private final ConfigurationDescriptionInteger remotePort = new ConfigurationDescriptionInteger(null, SeleniumCoreConstatns.CONFIG_SELENIUM_REMOTE_PORT, "Selenium Remote Port");

	private final ConfigurationDescriptionBoolean local = new ConfigurationDescriptionBoolean(false, SeleniumCoreConstatns.CONFIG_SELENIUM_LOCAL, "Selenium Local");

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
		final Set<ConfigurationDescription> result = new HashSet<ConfigurationDescription>();
		result.add(remoteHost);
		result.add(remotePort);
		result.add(local);
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

	@Override
	public Boolean getSeleniumLocal() {
		return getValueBoolean(local);
	}

}
