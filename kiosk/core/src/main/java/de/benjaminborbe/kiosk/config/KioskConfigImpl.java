package de.benjaminborbe.kiosk.config;

import de.benjaminborbe.configuration.api.ConfigurationDescription;
import de.benjaminborbe.configuration.api.ConfigurationService;
import de.benjaminborbe.configuration.tools.ConfigurationBase;
import de.benjaminborbe.configuration.tools.ConfigurationDescriptionBoolean;
import de.benjaminborbe.kiosk.KioskConstants;
import de.benjaminborbe.tools.util.ParseUtil;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class KioskConfigImpl extends ConfigurationBase implements KioskConfig {

	private final ConfigurationDescriptionBoolean kioskEnabled = new ConfigurationDescriptionBoolean(false, KioskConstants.CONFIG_KIOSK_CONNECTOR_ENABLED, "Kiosk Connector Enabled");

	@Inject
	public KioskConfigImpl(
		final Logger logger,
		final ConfigurationService configurationService,
		final ParseUtil parseUtil
	) {
		super(logger, parseUtil, configurationService);
	}

	@Override
	public Collection<ConfigurationDescription> getConfigurations() {
		final Set<ConfigurationDescription> result = new HashSet<ConfigurationDescription>();
		result.add(kioskEnabled);
		return result;
	}

	@Override
	public boolean isKioskConnectorEnabled() {
		return getValueBoolean(kioskEnabled);
	}
}
