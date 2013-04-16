package de.benjaminborbe.util.core;

import com.google.inject.Inject;
import de.benjaminborbe.tools.guice.Modules;
import de.benjaminborbe.tools.osgi.BaseBundleActivator;
import de.benjaminborbe.tools.osgi.ServiceInfo;
import de.benjaminborbe.util.api.UtilService;
import de.benjaminborbe.util.core.guice.UtilModules;
import org.osgi.framework.BundleContext;
import org.osgi.util.tracker.ServiceTracker;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class UtilActivator extends BaseBundleActivator {

	@Inject
	private UtilService utilService;

	@Override
	protected Modules getModules(final BundleContext context) {
		return new UtilModules(context);
	}

	@Override
	public Collection<ServiceInfo> getServiceInfos() {
		final Set<ServiceInfo> result = new HashSet<>(super.getServiceInfos());
		result.add(new ServiceInfo(UtilService.class, utilService));
		return result;
	}

	@Override
	public Collection<ServiceTracker> getServiceTrackers(final BundleContext context) {
		final Set<ServiceTracker> serviceTrackers = new HashSet<>(super.getServiceTrackers(context));
		// serviceTrackers.add(new UtilServiceTracker(utilRegistry, context,
		// UtilService.class));
		return serviceTrackers;
	}
}
