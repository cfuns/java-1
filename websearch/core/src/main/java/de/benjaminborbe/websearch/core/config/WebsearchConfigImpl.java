package de.benjaminborbe.websearch.core.config;

import de.benjaminborbe.configuration.api.ConfigurationDescription;
import de.benjaminborbe.configuration.api.ConfigurationService;
import de.benjaminborbe.configuration.tools.ConfigurationBase;
import de.benjaminborbe.configuration.tools.ConfigurationDescriptionBoolean;
import de.benjaminborbe.configuration.tools.ConfigurationDescriptionInteger;
import de.benjaminborbe.tools.util.ParseUtil;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class WebsearchConfigImpl extends ConfigurationBase implements WebsearchConfig {

	private final ConfigurationDescriptionInteger refreshLimit = new ConfigurationDescriptionInteger(-1, "WebsearchRefreshLimit", "Websearch Refresh Limit");

	private final ConfigurationDescriptionBoolean cronEnabled = new ConfigurationDescriptionBoolean(false, "WebsearchCronEnabled", "Websearch Cron Enabled");

	private final ConfigurationDescriptionBoolean saveImages = new ConfigurationDescriptionBoolean(false, "WebsearchSaveImages", "Websearch Save Images");

	private final ConfigurationDescriptionInteger minImageShortSide = new ConfigurationDescriptionInteger(600, "WebsearchMinImageShortSide", "Websearch Min Image Short Side");

	private final ConfigurationDescriptionInteger minImageLongSide = new ConfigurationDescriptionInteger(900, "WebsearchMinImageLongSide", "Websearch Min Image Long Side");

	@Inject
	public WebsearchConfigImpl(
		final Logger logger,
		final ConfigurationService configurationService,
		final ParseUtil parseUtil
	) {
		super(logger, parseUtil, configurationService);
	}

	@Override
	public Collection<ConfigurationDescription> getConfigurations() {
		final Set<ConfigurationDescription> result = new HashSet<ConfigurationDescription>();
		result.add(refreshLimit);
		result.add(cronEnabled);
		result.add(saveImages);
		result.add(minImageShortSide);
		result.add(minImageLongSide);
		return result;
	}

	@Override
	public Integer getRefreshLimit() {
		return getValueInteger(refreshLimit);
	}

	@Override
	public boolean getCronEnabled() {
		return Boolean.TRUE.equals(getValueBoolean(cronEnabled));
	}

	@Override
	public boolean saveImages() {
		return Boolean.TRUE.equals(getValueBoolean(saveImages));
	}

	@Override
	public int minImageLongSide() {
		return getValueInteger(minImageLongSide);
	}

	@Override
	public int minImageShortSide() {
		return getValueInteger(minImageShortSide);
	}

}
