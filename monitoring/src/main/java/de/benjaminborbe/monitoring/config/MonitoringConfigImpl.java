package de.benjaminborbe.monitoring.config;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;

import com.google.inject.Inject;

import de.benjaminborbe.configuration.api.ConfigurationDescription;
import de.benjaminborbe.configuration.api.ConfigurationService;
import de.benjaminborbe.configuration.tools.ConfigurationBase;
import de.benjaminborbe.configuration.tools.ConfigurationDescriptionString;
import de.benjaminborbe.tools.util.ParseUtil;

public class MonitoringConfigImpl extends ConfigurationBase implements MonitoringConfig {

	private final ConfigurationDescriptionString azUsername = new ConfigurationDescriptionString("username", "MonitoringAzUsername", "Monitoring Username for AZ");

	private final ConfigurationDescriptionString azPassword = new ConfigurationDescriptionString("password", "MonitoringAzPassword", "Monitoring Password for AZ");

	private final ConfigurationDescriptionString twentyfeetUsername = new ConfigurationDescriptionString("username", "MonitoringTwentyfeetUsername",
			"Monitoring Username for Twentyfeet");

	private final ConfigurationDescriptionString twentyfeetPassword = new ConfigurationDescriptionString("password", "MonitoringTwentyfeetPassword",
			"Monitoring Password for Twentyfeet");

	@Inject
	public MonitoringConfigImpl(final Logger logger, final ConfigurationService configurationService, final ParseUtil parseUtil) {
		super(logger, configurationService, parseUtil);
	}

	@Override
	public Collection<ConfigurationDescription> getConfigurations() {
		final Set<ConfigurationDescription> result = new HashSet<ConfigurationDescription>();
		result.add(azUsername);
		result.add(azPassword);
		return result;
	}

	@Override
	public String getAzUsername() {
		return getValueString(azUsername);
	}

	@Override
	public String getAzPassword() {
		return getValueString(azPassword);
	}

	@Override
	public String getTwentyfeetAdminUsername() {
		return getValueString(twentyfeetUsername);
	}

	@Override
	public String getTwentyfeetAdminPassword() {
		return getValueString(twentyfeetPassword);
	}

}
