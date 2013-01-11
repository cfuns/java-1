package de.benjaminborbe.analytics;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.osgi.framework.BundleContext;
import org.osgi.util.tracker.ServiceTracker;

import com.google.inject.Inject;

import de.benjaminborbe.analytics.api.AnalyticsService;
import de.benjaminborbe.analytics.guice.AnalyticsModules;
import de.benjaminborbe.tools.guice.Modules;
import de.benjaminborbe.tools.osgi.BaseBundleActivator;
import de.benjaminborbe.tools.osgi.ServiceInfo;

public class AnalyticsActivator extends BaseBundleActivator {

	@Inject
	private AnalyticsService analyticsService;

	@Override
	protected Modules getModules(final BundleContext context) {
		return new AnalyticsModules(context);
	}

	@Override
	public Collection<ServiceInfo> getServiceInfos() {
		final Set<ServiceInfo> result = new HashSet<ServiceInfo>(super.getServiceInfos());
		result.add(new ServiceInfo(AnalyticsService.class, analyticsService));
		return result;
	}

	@Override
	public Collection<ServiceTracker> getServiceTrackers(final BundleContext context) {
		final Set<ServiceTracker> serviceTrackers = new HashSet<ServiceTracker>(super.getServiceTrackers(context));
		// serviceTrackers.add(new AnalyticsServiceTracker(analyticsRegistry, context,
		// AnalyticsService.class));
		return serviceTrackers;
	}
}
