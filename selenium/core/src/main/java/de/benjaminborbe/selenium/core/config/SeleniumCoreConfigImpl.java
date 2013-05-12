package de.benjaminborbe.selenium.core.config;

import de.benjaminborbe.configuration.api.ConfigurationDescription;
import de.benjaminborbe.configuration.tools.ConfigurationBase;
import de.benjaminborbe.configuration.tools.ConfigurationDescriptionBoolean;
import de.benjaminborbe.configuration.tools.ConfigurationServiceCache;
import de.benjaminborbe.tools.util.ParseUtil;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class SeleniumCoreConfigImpl extends ConfigurationBase implements SeleniumCoreConfig {

	private final ConfigurationDescriptionBoolean useRemoteDriver = new ConfigurationDescriptionBoolean(false, "SeleniumUseRemoteDriver", "Selenium Use Remote Driver");

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
		result.add(useRemoteDriver);
		return result;
	}

	@Override
	public boolean getUseRemoteDriver() {
		return Boolean.TRUE.equals(getValueBoolean(useRemoteDriver));
	}

}
