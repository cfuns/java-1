package de.benjaminborbe.vnc;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.osgi.framework.BundleContext;
import org.osgi.util.tracker.ServiceTracker;

import com.google.inject.Inject;

import de.benjaminborbe.tools.guice.Modules;
import de.benjaminborbe.tools.osgi.BaseBundleActivator;
import de.benjaminborbe.tools.osgi.ServiceInfo;
import de.benjaminborbe.vnc.api.VncService;
import de.benjaminborbe.vnc.guice.VncModules;

public class VncActivator extends BaseBundleActivator {

	@Inject
	private VncService vncService;

	@Override
	protected Modules getModules(final BundleContext context) {
		return new VncModules(context);
	}

	@Override
	public Collection<ServiceInfo> getServiceInfos() {
		final Set<ServiceInfo> result = new HashSet<ServiceInfo>(super.getServiceInfos());
		result.add(new ServiceInfo(VncService.class, vncService));
		return result;
	}

	@Override
	public Collection<ServiceTracker> getServiceTrackers(final BundleContext context) {
		final Set<ServiceTracker> serviceTrackers = new HashSet<ServiceTracker>(super.getServiceTrackers(context));
		// serviceTrackers.add(new VncServiceTracker(vncRegistry, context,
		// VncService.class));
		return serviceTrackers;
	}
}
