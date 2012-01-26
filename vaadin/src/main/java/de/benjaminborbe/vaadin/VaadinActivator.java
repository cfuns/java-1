package de.benjaminborbe.vaadin;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.osgi.framework.BundleContext;
import org.osgi.util.tracker.ServiceTracker;

import com.google.inject.Inject;

import de.benjaminborbe.vaadin.guice.VaadinModules;
import de.benjaminborbe.vaadin.servlet.VaadinServlet;
import de.benjaminborbe.tools.guice.Modules;
import de.benjaminborbe.tools.osgi.FilterInfo;
import de.benjaminborbe.tools.osgi.HttpBundleActivator;
import de.benjaminborbe.tools.osgi.ResourceInfo;
import de.benjaminborbe.tools.osgi.ServiceInfo;
import de.benjaminborbe.tools.osgi.ServletInfo;

public class VaadinActivator extends HttpBundleActivator {

	@Inject
	private VaadinServlet vaadinServlet;

	public VaadinActivator() {
		super("vaadin");
	}

	@Override
	protected Modules getModules(final BundleContext context) {
		return new VaadinModules(context);
	}

	@Override
	protected Collection<ServletInfo> getServletInfos() {
		final Set<ServletInfo> result = new HashSet<ServletInfo>(super.getServletInfos());
		result.add(new ServletInfo(vaadinServlet, "/"));
		return result;
	}

	@Override
	protected Collection<FilterInfo> getFilterInfos() {
		final Set<FilterInfo> result = new HashSet<FilterInfo>(super.getFilterInfos());
		// result.add(new FilterInfo(vaadinFilter, ".*", 1));
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
		// result.add(new ServiceInfo(VaadinService.class, vaadinService));
		return result;
	}

	@Override
	protected Collection<ServiceTracker> getServiceTrackers(final BundleContext context) {
		final Set<ServiceTracker> serviceTrackers = new HashSet<ServiceTracker>(super.getServiceTrackers(context));
		// serviceTrackers.add(new VaadinServiceTracker(vaadinRegistry, context,
		// VaadinService.class));
		return serviceTrackers;
	}
}
