package de.benjaminborbe.microblog.config;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.configuration.api.ConfigurationDescription;
import de.benjaminborbe.configuration.api.ConfigurationService;
import de.benjaminborbe.configuration.tools.ConfigurationBase;
import de.benjaminborbe.configuration.tools.ConfigurationDescriptionBoolean;
import de.benjaminborbe.microblog.MicroblogConstants;
import de.benjaminborbe.tools.util.ParseUtil;

@Singleton
public class MicroblogConfigImpl extends ConfigurationBase implements MicroblogConfig {

	private final ConfigurationDescriptionBoolean xmppEnabled = new ConfigurationDescriptionBoolean(false, MicroblogConstants.CONFIG_XMPP_ENABLED, "Microblog Xmpp Enabled");

	private final ConfigurationDescriptionBoolean mailEnabled = new ConfigurationDescriptionBoolean(false, MicroblogConstants.CONFIG_MAIL_ENABLED, "Microblog Mail Enabled");

	private final ConfigurationDescriptionBoolean cronEnabled = new ConfigurationDescriptionBoolean(false, "MicroblogCronEnabled", "Microblog Cron Enabled");

	@Inject
	public MicroblogConfigImpl(final Logger logger, final ConfigurationService configurationService, final ParseUtil parseUtil) {
		super(logger, configurationService, parseUtil);
	}

	@Override
	public Collection<ConfigurationDescription> getConfigurations() {
		final Set<ConfigurationDescription> result = new HashSet<ConfigurationDescription>();
		result.add(xmppEnabled);
		result.add(cronEnabled);
		result.add(mailEnabled);
		return result;
	}

	@Override
	public boolean isXmppEnabled() {
		return getValueBoolean(xmppEnabled);
	}

	@Override
	public boolean isMailEnabled() {
		return getValueBoolean(mailEnabled);
	}

	@Override
	public boolean isCronEnabled() {
		return getValueBoolean(cronEnabled);
	}
}
