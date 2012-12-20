package de.benjaminborbe.projectile.config;

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
import de.benjaminborbe.projectile.ProjectileConstants;
import de.benjaminborbe.tools.util.ParseUtil;

@Singleton
public class ProjectileConfigImpl extends ConfigurationBase implements ProjectileConfig {

	private final ConfigurationDescriptionString authToken = new ConfigurationDescriptionString(null, ProjectileConstants.CONFIG_AUTH_TOKEN, "Projectile Auth Token");

	private final ConfigurationDescriptionString pop3Login = new ConfigurationDescriptionString(null, ProjectileConstants.CONFIG_POP3_LOGIN, "Projectile Pop3 Login");

	private final ConfigurationDescriptionString pop3Password = new ConfigurationDescriptionString(null, ProjectileConstants.CONFIG_POP3_PASSWORD, "Projectile Pop3 Password");

	private final ConfigurationDescriptionString pop3Hostname = new ConfigurationDescriptionString(null, ProjectileConstants.CONFIG_POP3_HOSTNAME, "Projectile Pop3 Hostname");

	@Inject
	public ProjectileConfigImpl(final Logger logger, final ConfigurationService configurationService, final ParseUtil parseUtil) {
		super(logger, configurationService, parseUtil);
	}

	@Override
	public Collection<ConfigurationDescription> getConfigurations() {
		final Set<ConfigurationDescription> result = new HashSet<ConfigurationDescription>();
		result.add(authToken);
		result.add(pop3Login);
		result.add(pop3Password);
		result.add(pop3Hostname);
		return result;
	}

	@Override
	public String getPop3Login() {
		return getValueString(pop3Login);
	}

	@Override
	public String getPop3Password() {
		return getValueString(pop3Password);
	}

	@Override
	public String getPop3Hostname() {
		return getValueString(pop3Hostname);
	}

	@Override
	public String getAuthToken() {
		return getValueString(authToken);
	}

}
