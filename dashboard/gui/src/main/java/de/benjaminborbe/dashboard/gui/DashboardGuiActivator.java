package de.benjaminborbe.dashboard.gui;

import com.google.inject.Inject;
import de.benjaminborbe.dashboard.api.DashboardContentWidget;
import de.benjaminborbe.dashboard.api.DashboardWidget;
import de.benjaminborbe.dashboard.gui.guice.DashboardGuiModules;
import de.benjaminborbe.dashboard.gui.service.DashboardGuiWidgetRegistry;
import de.benjaminborbe.dashboard.gui.service.DashboardGuiWidgetServiceTracker;
import de.benjaminborbe.dashboard.gui.servlet.DashboardGuiConfigureServlet;
import de.benjaminborbe.dashboard.gui.servlet.DashboardGuiServlet;
import de.benjaminborbe.dashboard.gui.util.DashboardGuiNavigationEntry;
import de.benjaminborbe.navigation.api.NavigationEntry;
import de.benjaminborbe.tools.guice.Modules;
import de.benjaminborbe.tools.osgi.HttpBundleActivator;
import de.benjaminborbe.tools.osgi.ResourceInfo;
import de.benjaminborbe.tools.osgi.ServiceInfo;
import de.benjaminborbe.tools.osgi.ServletInfo;
import org.osgi.framework.BundleContext;
import org.osgi.util.tracker.ServiceTracker;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class DashboardGuiActivator extends HttpBundleActivator {

	@Inject
	private DashboardGuiConfigureServlet dashboardGuiConfigureServlet;

	@Inject
	private DashboardWidget dashboardWidget;

	@Inject
	private DashboardGuiWidgetRegistry dashboardWidgetRegistry;

	@Inject
	private DashboardGuiServlet dashboardServlet;

	@Inject
	private DashboardGuiNavigationEntry dashboardGuiNavigationEntry;

	public DashboardGuiActivator() {
		super(DashboardGuiConstants.NAME);
	}

	@Override
	protected Modules getModules(final BundleContext context) {
		return new DashboardGuiModules(context);
	}

	@Override
	protected Collection<ServletInfo> getServletInfos() {
		final Set<ServletInfo> result = new HashSet<>(super.getServletInfos());
		result.add(new ServletInfo(dashboardServlet, DashboardGuiConstants.URL_HOME));
		result.add(new ServletInfo(dashboardGuiConfigureServlet, DashboardGuiConstants.URL_CONFIGURE));
		return result;
	}

	@Override
	protected Collection<ResourceInfo> getResouceInfos() {
		final Set<ResourceInfo> result = new HashSet<>(super.getResouceInfos());
		result.add(new ResourceInfo("/css", "css"));
		return result;
	}

	@Override
	public Collection<ServiceInfo> getServiceInfos() {
		final Set<ServiceInfo> result = new HashSet<>(super.getServiceInfos());
		result.add(new ServiceInfo(DashboardWidget.class, dashboardWidget));
		result.add(new ServiceInfo(NavigationEntry.class, dashboardGuiNavigationEntry));
		return result;
	}

	@Override
	public Collection<ServiceTracker> getServiceTrackers(final BundleContext context) {
		final Set<ServiceTracker> serviceTrackers = new HashSet<>(super.getServiceTrackers(context));
		serviceTrackers.add(new DashboardGuiWidgetServiceTracker(dashboardWidgetRegistry, context, DashboardContentWidget.class));
		return serviceTrackers;
	}

}
