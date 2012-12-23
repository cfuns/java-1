package de.benjaminborbe.monitoring.gui;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.osgi.framework.BundleContext;

import com.google.inject.Inject;

import de.benjaminborbe.dashboard.api.DashboardContentWidget;
import de.benjaminborbe.monitoring.gui.guice.MonitoringGuiModules;
import de.benjaminborbe.monitoring.gui.service.MonitoringGuiDashboardWidget;
import de.benjaminborbe.monitoring.gui.service.MonitoringGuiNavigationEntry;
import de.benjaminborbe.monitoring.gui.servlet.MonitoringGuiCacheServlet;
import de.benjaminborbe.monitoring.gui.servlet.MonitoringGuiLiveServlet;
import de.benjaminborbe.monitoring.gui.servlet.MonitoringGuiSendmailServlet;
import de.benjaminborbe.monitoring.gui.servlet.MonitoringGuiSilentCheckServlet;
import de.benjaminborbe.navigation.api.NavigationEntry;
import de.benjaminborbe.tools.guice.Modules;
import de.benjaminborbe.tools.osgi.HttpBundleActivator;
import de.benjaminborbe.tools.osgi.ResourceInfo;
import de.benjaminborbe.tools.osgi.ServiceInfo;
import de.benjaminborbe.tools.osgi.ServletInfo;

public class MonitoringGuiActivator extends HttpBundleActivator {

	@Inject
	private MonitoringGuiLiveServlet monitoringLiveServlet;

	@Inject
	private MonitoringGuiCacheServlet monitoringCacheServlet;

	@Inject
	private MonitoringGuiDashboardWidget monitoringDashboardWidget;

	@Inject
	private MonitoringGuiSilentCheckServlet monitoringGuiSilentCheckServlet;

	@Inject
	private MonitoringGuiSendmailServlet monitoringGuiSendmailServlet;

	@Inject
	private MonitoringGuiNavigationEntry monitoringGuiNavigationEntry;

	public MonitoringGuiActivator() {
		super(MonitoringGuiConstants.NAME);
	}

	@Override
	protected Modules getModules(final BundleContext context) {
		return new MonitoringGuiModules(context);
	}

	@Override
	public Collection<ServiceInfo> getServiceInfos() {
		final Set<ServiceInfo> result = new HashSet<ServiceInfo>(super.getServiceInfos());
		result.add(new ServiceInfo(DashboardContentWidget.class, monitoringDashboardWidget, monitoringDashboardWidget.getClass().getName()));
		result.add(new ServiceInfo(NavigationEntry.class, monitoringGuiNavigationEntry, monitoringGuiNavigationEntry.getClass().getName()));

		return result;
	}

	@Override
	protected Collection<ServletInfo> getServletInfos() {
		final Set<ServletInfo> result = new HashSet<ServletInfo>(super.getServletInfos());
		result.add(new ServletInfo(monitoringCacheServlet, "/"));
		result.add(new ServletInfo(monitoringLiveServlet, "/live"));
		result.add(new ServletInfo(monitoringGuiSilentCheckServlet, "/silent"));
		result.add(new ServletInfo(monitoringGuiSendmailServlet, "/sendmail"));
		return result;
	}

	@Override
	protected Collection<ResourceInfo> getResouceInfos() {
		final Set<ResourceInfo> result = new HashSet<ResourceInfo>(super.getResouceInfos());
		result.add(new ResourceInfo("/css", "css"));
		return result;
	}

}
