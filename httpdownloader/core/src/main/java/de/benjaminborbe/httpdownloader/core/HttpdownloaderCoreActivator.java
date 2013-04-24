package de.benjaminborbe.httpdownloader.core;

import de.benjaminborbe.httpdownloader.api.HttpdownloaderService;
import de.benjaminborbe.httpdownloader.core.guice.HttpdownloaderCoreModules;
import de.benjaminborbe.tools.guice.Modules;
import de.benjaminborbe.tools.osgi.BaseBundleActivator;
import de.benjaminborbe.tools.osgi.ServiceInfo;
import org.osgi.framework.BundleContext;
import org.osgi.util.tracker.ServiceTracker;

import javax.inject.Inject;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class HttpdownloaderCoreActivator extends BaseBundleActivator {

	@Inject
	private HttpdownloaderService httpdownloaderService;

	@Override
	protected Modules getModules(final BundleContext context) {
		return new HttpdownloaderCoreModules(context);
	}

	@Override
	public Collection<ServiceInfo> getServiceInfos() {
		final Set<ServiceInfo> result = new HashSet<>(super.getServiceInfos());
		result.add(new ServiceInfo(HttpdownloaderService.class, httpdownloaderService));
		return result;
	}

	@Override
	public Collection<ServiceTracker> getServiceTrackers(final BundleContext context) {
		final Set<ServiceTracker> serviceTrackers = new HashSet<>(super.getServiceTrackers(context));
		// serviceTrackers.add(new HttpdownloaderServiceTracker(httpdownloaderRegistry, context,
		// HttpdownloaderService.class));
		return serviceTrackers;
	}
}
