package de.benjaminborbe.performance;

import de.benjaminborbe.performance.api.PerformanceService;
import de.benjaminborbe.performance.guice.PerformanceModules;
import de.benjaminborbe.tools.guice.Modules;
import de.benjaminborbe.tools.osgi.BaseBundleActivator;
import de.benjaminborbe.tools.osgi.ServiceInfo;
import org.osgi.framework.BundleContext;

import javax.inject.Inject;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class PerformanceActivator extends BaseBundleActivator {

	@Inject
	private PerformanceService performanceService;

	@Override
	protected Modules getModules(final BundleContext context) {
		return new PerformanceModules(context);
	}

	@Override
	public Collection<ServiceInfo> getServiceInfos() {
		final Set<ServiceInfo> result = new HashSet<>(super.getServiceInfos());
		result.add(new ServiceInfo(PerformanceService.class, performanceService));
		return result;
	}

}
