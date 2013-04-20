package de.benjaminborbe.authentication.core;

import javax.inject.Inject;
import de.benjaminborbe.authentication.api.AuthenticationService;
import de.benjaminborbe.authentication.core.config.AuthenticationConfig;
import de.benjaminborbe.authentication.core.guice.AuthenticationModules;
import de.benjaminborbe.configuration.api.ConfigurationDescription;
import de.benjaminborbe.tools.guice.Modules;
import de.benjaminborbe.tools.osgi.BaseBundleActivator;
import de.benjaminborbe.tools.osgi.ServiceInfo;
import org.osgi.framework.BundleContext;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class AuthenticationActivator extends BaseBundleActivator {

	@Inject
	private AuthenticationService authenticationService;

	@Inject
	private AuthenticationConfig authenticationConfig;

	@Override
	protected Modules getModules(final BundleContext context) {
		return new AuthenticationModules(context);
	}

	@Override
	public Collection<ServiceInfo> getServiceInfos() {
		final Set<ServiceInfo> result = new HashSet<>(super.getServiceInfos());
		result.add(new ServiceInfo(AuthenticationService.class, authenticationService));
		for (final ConfigurationDescription configuration : authenticationConfig.getConfigurations()) {
			result.add(new ServiceInfo(ConfigurationDescription.class, configuration, configuration.getName()));
		}
		return result;
	}

}
