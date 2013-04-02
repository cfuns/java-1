package de.benjaminborbe.proxy.core;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import de.benjaminborbe.proxy.core.guice.ProxyCoreModules;
import org.osgi.framework.BundleContext;
import org.osgi.util.tracker.ServiceTracker;

import com.google.inject.Inject;

import de.benjaminborbe.proxy.api.ProxyService;
import de.benjaminborbe.tools.guice.Modules;
import de.benjaminborbe.tools.osgi.BaseBundleActivator;
import de.benjaminborbe.tools.osgi.ServiceInfo;

public class ProxyCoreActivator extends BaseBundleActivator {

	@Inject
	private ProxyService proxyService;

	@Override
	protected Modules getModules(final BundleContext context) {
		return new ProxyCoreModules(context);
	}

	@Override
	public Collection<ServiceInfo> getServiceInfos() {
		final Set<ServiceInfo> result = new HashSet<ServiceInfo>(super.getServiceInfos());
		result.add(new ServiceInfo(ProxyService.class, proxyService));
		return result;
	}

	@Override
	public Collection<ServiceTracker> getServiceTrackers(final BundleContext context) {
		final Set<ServiceTracker> serviceTrackers = new HashSet<ServiceTracker>(super.getServiceTrackers(context));
		// serviceTrackers.add(new ProxyServiceTracker(proxyRegistry, context,
		// ProxyService.class));
		return serviceTrackers;
	}
}
