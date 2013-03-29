package de.benjaminborbe.sample.core;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import de.benjaminborbe.sample.core.guice.SampleCoreModules;
import org.osgi.framework.BundleContext;
import org.osgi.util.tracker.ServiceTracker;

import com.google.inject.Inject;

import de.benjaminborbe.sample.api.SampleService;
import de.benjaminborbe.tools.guice.Modules;
import de.benjaminborbe.tools.osgi.BaseBundleActivator;
import de.benjaminborbe.tools.osgi.ServiceInfo;

public class SampleCoreActivator extends BaseBundleActivator {

	@Inject
	private SampleService sampleService;

	@Override
	protected Modules getModules(final BundleContext context) {
		return new SampleCoreModules(context);
	}

	@Override
	public Collection<ServiceInfo> getServiceInfos() {
		final Set<ServiceInfo> result = new HashSet<ServiceInfo>(super.getServiceInfos());
		result.add(new ServiceInfo(SampleService.class, sampleService));
		return result;
	}

	@Override
	public Collection<ServiceTracker> getServiceTrackers(final BundleContext context) {
		final Set<ServiceTracker> serviceTrackers = new HashSet<ServiceTracker>(super.getServiceTrackers(context));
		// serviceTrackers.add(new SampleServiceTracker(sampleRegistry, context,
		// SampleService.class));
		return serviceTrackers;
	}
}
