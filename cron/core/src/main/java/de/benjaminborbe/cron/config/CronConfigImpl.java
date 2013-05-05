package de.benjaminborbe.cron.config;

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

public class CronConfigImpl extends ConfigurationBase implements CronConfig {

	private final ConfigurationDescriptionBoolean autoStart = new ConfigurationDescriptionBoolean(false, "CronAutoStart", "Cron Auto Start");

	@Inject
	public CronConfigImpl(
		final Logger logger,
		final ParseUtil parseUtil,
		final ConfigurationServiceCache configurationServiceCache
	) {
		super(logger, parseUtil, configurationServiceCache);
	}

	@Override
	public Collection<ConfigurationDescription> getConfigurations() {
		final Set<ConfigurationDescription> result = new HashSet<>();
		result.add(autoStart);
		return result;
	}

	@Override
	public boolean autoStart() {
		return Boolean.TRUE.equals(getValueBoolean(autoStart));
	}

}
