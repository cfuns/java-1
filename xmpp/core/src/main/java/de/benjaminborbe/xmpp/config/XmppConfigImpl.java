package de.benjaminborbe.xmpp.config;

import de.benjaminborbe.configuration.api.ConfigurationDescription;
import de.benjaminborbe.configuration.api.ConfigurationService;
import de.benjaminborbe.configuration.tools.ConfigurationBase;
import de.benjaminborbe.configuration.tools.ConfigurationDescriptionInteger;
import de.benjaminborbe.configuration.tools.ConfigurationDescriptionString;
import de.benjaminborbe.tools.util.ParseUtil;
import de.benjaminborbe.xmpp.XmppConstants;
import org.slf4j.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Singleton
public class XmppConfigImpl extends ConfigurationBase implements XmppConfig {

	private final ConfigurationDescriptionString username = new ConfigurationDescriptionString(null, XmppConstants.CONFIG_USERNAME, "Xmpp Username");

	private final ConfigurationDescriptionString password = new ConfigurationDescriptionString(null, XmppConstants.CONFIG_PASSWORD, "Xmpp Password");

	private final ConfigurationDescriptionString serverHost = new ConfigurationDescriptionString(null, XmppConstants.CONFIG_SERVERHOST, "Xmpp Host for Server");

	private final ConfigurationDescriptionInteger serverPort = new ConfigurationDescriptionInteger(5222, XmppConstants.CONFIG_SERVERPORT, "Xmpp Port for Server");

	@Inject
	public XmppConfigImpl(
		final Logger logger,
		final ConfigurationService configurationService,
		final ParseUtil parseUtil
	) {
		super(logger, parseUtil, configurationService);
	}

	@Override
	public String getUsername() {
		return getValueString(username);
	}

	@Override
	public String getPassword() {
		return getValueString(password);
	}

	@Override
	public String getServerHost() {
		return getValueString(serverHost);
	}

	@Override
	public Integer getServerPort() {
		return getValueInteger(serverPort);
	}

	@Override
	public Collection<ConfigurationDescription> getConfigurations() {
		final Set<ConfigurationDescription> result = new HashSet<ConfigurationDescription>();
		result.add(username);
		result.add(password);
		result.add(serverHost);
		result.add(serverPort);
		return result;
	}
}
