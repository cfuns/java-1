package de.benjaminborbe.virt.core;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import de.benjaminborbe.virt.core.guice.VirtCoreModules;
import org.osgi.framework.BundleContext;
import org.osgi.util.tracker.ServiceTracker;

import com.google.inject.Inject;

import de.benjaminborbe.virt.api.VirtService;
import de.benjaminborbe.tools.guice.Modules;
import de.benjaminborbe.tools.osgi.BaseBundleActivator;
import de.benjaminborbe.tools.osgi.ServiceInfo;

public class VirtCoreActivator extends BaseBundleActivator {

	@Inject
	private VirtService virtService;

	@Override
	protected Modules getModules(final BundleContext context) {
		return new VirtCoreModules(context);
	}

	@Override
	public Collection<ServiceInfo> getServiceInfos() {
		final Set<ServiceInfo> result = new HashSet<ServiceInfo>(super.getServiceInfos());
		result.add(new ServiceInfo(VirtService.class, virtService));
		return result;
	}

	@Override
	public Collection<ServiceTracker> getServiceTrackers(final BundleContext context) {
		final Set<ServiceTracker> serviceTrackers = new HashSet<ServiceTracker>(super.getServiceTrackers(context));
		// serviceTrackers.add(new VirtServiceTracker(virtRegistry, context,
		// VirtService.class));
		return serviceTrackers;
	}
}
