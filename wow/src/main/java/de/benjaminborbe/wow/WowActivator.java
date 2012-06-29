package de.benjaminborbe.wow;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.osgi.framework.BundleContext;
import org.osgi.util.tracker.ServiceTracker;

import com.google.inject.Inject;

import de.benjaminborbe.tools.guice.Modules;
import de.benjaminborbe.tools.osgi.BaseBundleActivator;
import de.benjaminborbe.tools.osgi.ServiceInfo;
import de.benjaminborbe.wow.api.WowService;
import de.benjaminborbe.wow.guice.WowModules;

public class WowActivator extends BaseBundleActivator {

	@Inject
	private WowService wowService;

	@Override
	protected Modules getModules(final BundleContext context) {
		return new WowModules(context);
	}

	@Override
	public Collection<ServiceInfo> getServiceInfos() {
		final Set<ServiceInfo> result = new HashSet<ServiceInfo>(super.getServiceInfos());
		result.add(new ServiceInfo(WowService.class, wowService));
		return result;
	}

	@Override
	public Collection<ServiceTracker> getServiceTrackers(final BundleContext context) {
		final Set<ServiceTracker> serviceTrackers = new HashSet<ServiceTracker>(super.getServiceTrackers(context));
		// serviceTrackers.add(new WowServiceTracker(wowRegistry, context,
		// WowService.class));
		return serviceTrackers;
	}
}
