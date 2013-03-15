package de.benjaminborbe.microblog.gui.config;

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
import de.benjaminborbe.microblog.gui.MicroblogGuiConstants;
import de.benjaminborbe.tools.util.ParseUtil;

@Singleton
public class MicroblogGuiConfigImpl extends ConfigurationBase implements MicroblogGuiConfig {

	private final ConfigurationDescriptionString authToken = new ConfigurationDescriptionString(null, MicroblogGuiConstants.PARAEMTER_NOTIFICATION_TOKEN, "Microblog Auth Token");

	@Inject
	public MicroblogGuiConfigImpl(final Logger logger, final ConfigurationService configurationService, final ParseUtil parseUtil) {
		super(logger, configurationService, parseUtil);
	}

	@Override
	public Collection<ConfigurationDescription> getConfigurations() {
		final Set<ConfigurationDescription> result = new HashSet<ConfigurationDescription>();
		result.add(authToken);
		return result;
	}

	@Override
	public String getAuthToken() {
		return getValueString(authToken);
	}

}
