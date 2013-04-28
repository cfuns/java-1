package de.benjaminborbe.projectile.config;

import de.benjaminborbe.configuration.api.ConfigurationDescription;
import de.benjaminborbe.configuration.api.ConfigurationService;
import de.benjaminborbe.configuration.tools.ConfigurationBase;
import de.benjaminborbe.configuration.tools.ConfigurationDescriptionBoolean;
import de.benjaminborbe.configuration.tools.ConfigurationDescriptionString;
import de.benjaminborbe.configuration.tools.ConfigurationServiceCache;
import de.benjaminborbe.projectile.ProjectileConstants;
import de.benjaminborbe.tools.util.ParseUtil;
import org.slf4j.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Singleton
public class ProjectileConfigImpl extends ConfigurationBase implements ProjectileConfig {

	private final ConfigurationDescriptionString authToken = new ConfigurationDescriptionString(null, ProjectileConstants.CONFIG_AUTH_TOKEN, "Projectile Auth Token");

	private final ConfigurationDescriptionString pop3Login = new ConfigurationDescriptionString(null, ProjectileConstants.CONFIG_POP3_LOGIN, "Projectile Pop3 Login");

	private final ConfigurationDescriptionString pop3Password = new ConfigurationDescriptionString(null, ProjectileConstants.CONFIG_POP3_PASSWORD, "Projectile Pop3 Password");

	private final ConfigurationDescriptionString pop3Hostname = new ConfigurationDescriptionString(null, ProjectileConstants.CONFIG_POP3_HOSTNAME, "Projectile Pop3 Hostname");

	private final ConfigurationDescriptionBoolean pop3Delete = new ConfigurationDescriptionBoolean(false, ProjectileConstants.CONFIG_POP3_DELETE, "Projectile Pop3 Delete");

	private final ConfigurationDescriptionBoolean cronActive = new ConfigurationDescriptionBoolean(false, ProjectileConstants.CONFIG_CRON_ACTIVE, "Projectile Cron Active");

	@Inject
	public ProjectileConfigImpl(
		final Logger logger,
		final ConfigurationService configurationService,
		final ParseUtil parseUtil,
		final ConfigurationServiceCache configurationServiceCache
	) {
		super(logger, parseUtil, configurationServiceCache);
	}

	@Override
	public Collection<ConfigurationDescription> getConfigurations() {
		final Set<ConfigurationDescription> result = new HashSet<>();
		result.add(authToken);
		result.add(pop3Login);
		result.add(pop3Password);
		result.add(pop3Hostname);
		result.add(pop3Delete);
		result.add(cronActive);
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

	@Override
	public boolean getPop3Delete() {
		return getValueBoolean(pop3Delete);
	}

	@Override
	public Boolean getCronActive() {
		return getValueBoolean(cronActive);
	}

}
