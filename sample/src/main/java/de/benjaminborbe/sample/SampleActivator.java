package de.benjaminborbe.sample;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.osgi.framework.BundleContext;
import org.osgi.util.tracker.ServiceTracker;

import com.google.inject.Inject;

import de.benjaminborbe.sample.api.SampleService;
import de.benjaminborbe.sample.guice.SampleModules;
import de.benjaminborbe.tools.guice.Modules;
import de.benjaminborbe.tools.osgi.BaseBundleActivator;
import de.benjaminborbe.tools.osgi.ServiceInfo;

public class SampleActivator extends BaseBundleActivator {

	@Inject
	private SampleService sampleService;

	@Override
	protected Modules getModules(final BundleContext context) {
		return new SampleModules(context);
	}

	@Override
	protected Collection<ServiceInfo> getServiceInfos() {
		final Set<ServiceInfo> result = new HashSet<ServiceInfo>(super.getServiceInfos());
		result.add(new ServiceInfo(SampleService.class, sampleService));
		return result;
	}

	@Override
	protected Collection<ServiceTracker> getServiceTrackers(final BundleContext context) {
		final Set<ServiceTracker> serviceTrackers = new HashSet<ServiceTracker>(super.getServiceTrackers(context));
		// serviceTrackers.add(new SampleServiceTracker(sampleRegistry, context,
		// SampleService.class));
		return serviceTrackers;
	}
}
