package de.benjaminborbe.authorization;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.osgi.framework.BundleContext;
import org.osgi.util.tracker.ServiceTracker;

import com.google.inject.Inject;

import de.benjaminborbe.authorization.api.AuthorizationService;
import de.benjaminborbe.authorization.guice.AuthorizationModules;
import de.benjaminborbe.tools.guice.Modules;
import de.benjaminborbe.tools.osgi.BaseBundleActivator;
import de.benjaminborbe.tools.osgi.ServiceInfo;

public class AuthorizationActivator extends BaseBundleActivator {

	@Inject
	private AuthorizationService authorizationService;

	@Override
	protected Modules getModules(final BundleContext context) {
		return new AuthorizationModules(context);
	}

	@Override
	protected Collection<ServiceInfo> getServiceInfos() {
		final Set<ServiceInfo> result = new HashSet<ServiceInfo>(super.getServiceInfos());
		result.add(new ServiceInfo(AuthorizationService.class, authorizationService));
		return result;
	}

	@Override
	protected Collection<ServiceTracker> getServiceTrackers(final BundleContext context) {
		final Set<ServiceTracker> serviceTrackers = new HashSet<ServiceTracker>(super.getServiceTrackers(context));
		// serviceTrackers.add(new AuthorizationServiceTracker(authorizationRegistry, context,
		// AuthorizationService.class));
		return serviceTrackers;
	}
}
