package de.benjaminborbe.lunch;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.osgi.framework.BundleContext;
import org.osgi.util.tracker.ServiceTracker;

import com.google.inject.Inject;

import de.benjaminborbe.lunch.api.LunchService;
import de.benjaminborbe.lunch.guice.LunchModules;
import de.benjaminborbe.tools.guice.Modules;
import de.benjaminborbe.tools.osgi.BaseBundleActivator;
import de.benjaminborbe.tools.osgi.ServiceInfo;

public class LunchActivator extends BaseBundleActivator {

	@Inject
	private LunchService lunchService;

	@Override
	protected Modules getModules(final BundleContext context) {
		return new LunchModules(context);
	}

	@Override
	public Collection<ServiceInfo> getServiceInfos() {
		final Set<ServiceInfo> result = new HashSet<ServiceInfo>(super.getServiceInfos());
		result.add(new ServiceInfo(LunchService.class, lunchService));
		return result;
	}

	@Override
	public Collection<ServiceTracker> getServiceTrackers(final BundleContext context) {
		final Set<ServiceTracker> serviceTrackers = new HashSet<ServiceTracker>(super.getServiceTrackers(context));
		// serviceTrackers.add(new LunchServiceTracker(lunchRegistry, context,
		// LunchService.class));
		return serviceTrackers;
	}
}