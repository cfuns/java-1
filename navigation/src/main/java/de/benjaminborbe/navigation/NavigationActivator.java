package de.benjaminborbe.navigation;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.osgi.framework.BundleContext;
import org.osgi.util.tracker.ServiceTracker;

import com.google.inject.Inject;

import de.benjaminborbe.navigation.api.NavigationEntry;
import de.benjaminborbe.navigation.api.NavigationWidget;
import de.benjaminborbe.navigation.guice.NavigationModules;
import de.benjaminborbe.navigation.service.NavigationServiceTracker;
import de.benjaminborbe.navigation.servlet.NavigationServlet;
import de.benjaminborbe.navigation.util.NavigationEntryRegistry;
import de.benjaminborbe.tools.guice.Modules;
import de.benjaminborbe.tools.osgi.HttpBundleActivator;
import de.benjaminborbe.tools.osgi.ServiceInfo;
import de.benjaminborbe.tools.osgi.ServletInfo;

public class NavigationActivator extends HttpBundleActivator {

	@Inject
	private NavigationServlet navigationServlet;

	@Inject
	private NavigationEntryRegistry navigationEntryRegistry;

	@Inject
	private NavigationWidget navigationWidget;

	public NavigationActivator() {
		super("navigation");
	}

	@Override
	protected Modules getModules(final BundleContext context) {
		return new NavigationModules(context);
	}

	@Override
	protected Collection<ServletInfo> getServletInfos() {
		final Set<ServletInfo> result = new HashSet<ServletInfo>(super.getServletInfos());
		result.add(new ServletInfo(navigationServlet, "/"));
		return result;
	}

	@Override
	protected Collection<ServiceInfo> getServiceInfos() {
		final Set<ServiceInfo> result = new HashSet<ServiceInfo>(super.getServiceInfos());
		result.add(new ServiceInfo(NavigationWidget.class, navigationWidget));
		return result;
	}

	@Override
	protected Collection<ServiceTracker> getServiceTrackers(final BundleContext context) {
		final Set<ServiceTracker> serviceTrackers = new HashSet<ServiceTracker>(super.getServiceTrackers(context));
		serviceTrackers.add(new NavigationServiceTracker(navigationEntryRegistry, context, NavigationEntry.class));
		return serviceTrackers;
	}
}
