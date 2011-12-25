package de.benjaminborbe.util;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.osgi.framework.BundleContext;
import org.osgi.util.tracker.ServiceTracker;

import com.google.inject.Inject;

import de.benjaminborbe.util.guice.UtilModules;
import de.benjaminborbe.util.servlet.UtilPasswordGeneratorServlet;
import de.benjaminborbe.util.servlet.UtilServlet;
import de.benjaminborbe.tools.guice.Modules;
import de.benjaminborbe.tools.osgi.FilterInfo;
import de.benjaminborbe.tools.osgi.HttpBundleActivator;
import de.benjaminborbe.tools.osgi.ResourceInfo;
import de.benjaminborbe.tools.osgi.ServiceInfo;
import de.benjaminborbe.tools.osgi.ServletInfo;

public class UtilActivator extends HttpBundleActivator {

	@Inject
	private UtilServlet utilServlet;

	@Inject
	private UtilPasswordGeneratorServlet utilPasswordGeneratorServlet;

	public UtilActivator() {
		super("util");
	}

	@Override
	protected Modules getModules(final BundleContext context) {
		return new UtilModules(context);
	}

	@Override
	protected Collection<ServletInfo> getServletInfos() {
		final Set<ServletInfo> result = new HashSet<ServletInfo>(super.getServletInfos());
		result.add(new ServletInfo(utilServlet, "/"));
		result.add(new ServletInfo(utilPasswordGeneratorServlet, "/passwordGenerator"));
		return result;
	}

	@Override
	protected Collection<FilterInfo> getFilterInfos() {
		final Set<FilterInfo> result = new HashSet<FilterInfo>(super.getFilterInfos());
		// result.add(new FilterInfo(utilFilter, ".*", 1));
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
		// result.add(new ServiceInfo(UtilService.class, utilService));
		return result;
	}

	@Override
	protected Collection<ServiceTracker> getServiceTrackers(final BundleContext context) {
		final Set<ServiceTracker> serviceTrackers = new HashSet<ServiceTracker>(super.getServiceTrackers(context));
		// serviceTrackers.add(new UtilServiceTracker(utilRegistry, context,
		// UtilService.class));
		return serviceTrackers;
	}
}
