package de.benjaminborbe.shortener;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.osgi.framework.BundleContext;
import org.osgi.util.tracker.ServiceTracker;

import com.google.inject.Inject;

import de.benjaminborbe.shortener.api.ShortenerService;
import de.benjaminborbe.shortener.guice.ShortenerModules;
import de.benjaminborbe.tools.guice.Modules;
import de.benjaminborbe.tools.osgi.BaseBundleActivator;
import de.benjaminborbe.tools.osgi.ServiceInfo;

public class ShortenerActivator extends BaseBundleActivator {

	@Inject
	private ShortenerService shortenerService;

	@Override
	protected Modules getModules(final BundleContext context) {
		return new ShortenerModules(context);
	}

	@Override
	public Collection<ServiceInfo> getServiceInfos() {
		final Set<ServiceInfo> result = new HashSet<ServiceInfo>(super.getServiceInfos());
		result.add(new ServiceInfo(ShortenerService.class, shortenerService));
		return result;
	}

	@Override
	public Collection<ServiceTracker> getServiceTrackers(final BundleContext context) {
		final Set<ServiceTracker> serviceTrackers = new HashSet<ServiceTracker>(super.getServiceTrackers(context));
		// serviceTrackers.add(new ShortenerServiceTracker(shortenerRegistry, context,
		// ShortenerService.class));
		return serviceTrackers;
	}
}
