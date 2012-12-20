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

	@Inject
	public ProjectileConfigImpl(final Logger logger, final ConfigurationService configurationService, final ParseUtil parseUtil) {
		super(logger, configurationService, parseUtil);
	}

	@Override
	public String getAuthToken() {
		return getValueString(authToken);
	}

	@Override
	public Collection<ConfigurationDescription> getConfigurations() {
		final Set<ConfigurationDescription> result = new HashSet<ConfigurationDescription>();
		result.add(authToken);
		return result;
	}
}
