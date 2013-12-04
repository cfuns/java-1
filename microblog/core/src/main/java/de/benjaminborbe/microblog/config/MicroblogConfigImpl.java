package de.benjaminborbe.microblog.config;

import de.benjaminborbe.configuration.api.ConfigurationDescription;
import de.benjaminborbe.configuration.api.ConfigurationService;
import de.benjaminborbe.configuration.tools.ConfigurationBase;
import de.benjaminborbe.configuration.tools.ConfigurationDescriptionBoolean;
import de.benjaminborbe.configuration.tools.ConfigurationDescriptionString;
import de.benjaminborbe.microblog.MicroblogConstants;
import de.benjaminborbe.tools.util.ParseUtil;
import org.slf4j.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Singleton
public class MicroblogConfigImpl extends ConfigurationBase implements MicroblogConfig {

	private final ConfigurationDescriptionBoolean mailEnabled = new ConfigurationDescriptionBoolean(false, MicroblogConstants.CONFIG_MAIL_ENABLED, "Microblog Mail Enabled");

	private final ConfigurationDescriptionBoolean cronEnabled = new ConfigurationDescriptionBoolean(false, "MicroblogCronEnabled", "Microblog Cron Enabled");

	private final ConfigurationDescriptionString microblogRssFeed = new ConfigurationDescriptionString("https://micro.rp.seibert-media.net/api/statuses/public_timeline.rss",
		"MicroblogRssFeed", "Microblog Rss Feed");

	private final ConfigurationDescriptionString microblogUrl = new ConfigurationDescriptionString("https://micro.rp.seibert-media.net", "MicroblogUrl", "Microblog Url");

	@Inject
	public MicroblogConfigImpl(
		final Logger logger,
		final ConfigurationService configurationService,
		final ParseUtil parseUtil
	) {
		super(logger, parseUtil, configurationService);
	}

	@Override
	public Collection<ConfigurationDescription> getConfigurations() {
		final Set<ConfigurationDescription> result = new HashSet<ConfigurationDescription>();
		result.add(cronEnabled);
		result.add(mailEnabled);
		result.add(microblogRssFeed);
		result.add(microblogUrl);
		return result;
	}

	@Override
	public boolean isMailEnabled() {
		return getValueBoolean(mailEnabled);
	}

	@Override
	public boolean isCronEnabled() {
		return getValueBoolean(cronEnabled);
	}

	@Override
	public String getMicroblogRssFeed() {
		return getValueString(microblogRssFeed);
	}

	@Override
	public String getMicroblogUrl() {
		return getValueString(microblogUrl);
	}
}
