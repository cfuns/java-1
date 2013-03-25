package de.benjaminborbe.notification.config;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.configuration.api.ConfigurationDescription;
import de.benjaminborbe.configuration.api.ConfigurationService;
import de.benjaminborbe.configuration.tools.ConfigurationBase;
import de.benjaminborbe.configuration.tools.ConfigurationDescriptionString;
import de.benjaminborbe.notification.NotificationConstants;
import de.benjaminborbe.tools.util.ParseUtil;

@Singleton
public class NotificationConfigImpl extends ConfigurationBase implements NotificationConfig {

	private final ConfigurationDescriptionString emailFrom = new ConfigurationDescriptionString("noreply@example.com", NotificationConstants.CONFIG_EMAIL_FROM,
			"Notification Email From");

	@Inject
	public NotificationConfigImpl(final Logger logger, final ConfigurationService configurationService, final ParseUtil parseUtil) {
		super(logger, configurationService, parseUtil);
	}

	@Override
	public Collection<ConfigurationDescription> getConfigurations() {
		final Set<ConfigurationDescription> result = new HashSet<ConfigurationDescription>();
		result.add(emailFrom);
		return result;
	}

	@Override
	public String getEmailFrom() {
		return getValueString(emailFrom);
	}

}
