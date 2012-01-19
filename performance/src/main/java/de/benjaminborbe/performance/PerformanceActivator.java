package de.benjaminborbe.performance;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.osgi.framework.BundleContext;
import org.osgi.util.tracker.ServiceTracker;

import com.google.inject.Inject;

import de.benjaminborbe.performance.guice.PerformanceModules;
import de.benjaminborbe.performance.servlet.PerformanceFilter;
import de.benjaminborbe.performance.servlet.PerformanceServlet;
import de.benjaminborbe.tools.guice.Modules;
import de.benjaminborbe.tools.osgi.FilterInfo;
import de.benjaminborbe.tools.osgi.HttpBundleActivator;
import de.benjaminborbe.tools.osgi.ResourceInfo;
import de.benjaminborbe.tools.osgi.ServiceInfo;
import de.benjaminborbe.tools.osgi.ServletInfo;

public class PerformanceActivator extends HttpBundleActivator {

	@Inject
	private PerformanceServlet performanceServlet;

	@Inject
	private PerformanceFilter performanceFilter;

	public PerformanceActivator() {
		super("performance");
	}

	@Override
	protected Modules getModules(final BundleContext context) {
		return new PerformanceModules(context);
	}

	@Override
	protected Collection<ServletInfo> getServletInfos() {
		final Set<ServletInfo> result = new HashSet<ServletInfo>(super.getServletInfos());
		result.add(new ServletInfo(performanceServlet, "/"));
		return result;
	}

	@Override
	protected Collection<FilterInfo> getFilterInfos() {
		final Set<FilterInfo> result = new HashSet<FilterInfo>(super.getFilterInfos());
		result.add(new FilterInfo(performanceFilter, ".*", 998, true));
		return result;
	}

	@Override
	protected Collection<ResourceInfo> getResouceInfos() {
		final Set<ResourceInfo> result = new HashSet<ResourceInfo>(super.getResouceInfos());
		// result.add(new ResourceInfo("/css", "css"));
		// result.add(new ResourceInfo("/js", "js"));
		return result;
	}

	@Override
	protected Collection<ServiceInfo> getServiceInfos() {
		final Set<ServiceInfo> result = new HashSet<ServiceInfo>(super.getServiceInfos());
		// result.add(new ServiceInfo(PerformanceService.class, performanceService));
		return result;
	}

	@Override
	protected Collection<ServiceTracker> getServiceTrackers(final BundleContext context) {
		final Set<ServiceTracker> serviceTrackers = new HashSet<ServiceTracker>(super.getServiceTrackers(context));
		// serviceTrackers.add(new PerformanceServiceTracker(performanceRegistry, context,
		// PerformanceService.class));
		return serviceTrackers;
	}
}
