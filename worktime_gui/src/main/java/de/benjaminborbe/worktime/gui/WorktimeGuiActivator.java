package de.benjaminborbe.worktime.gui;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.osgi.framework.BundleContext;

import com.google.inject.Inject;

import de.benjaminborbe.dashboard.api.DashboardContentWidget;
import de.benjaminborbe.tools.guice.Modules;
import de.benjaminborbe.tools.osgi.HttpBundleActivator;
import de.benjaminborbe.tools.osgi.ServiceInfo;
import de.benjaminborbe.tools.osgi.ServletInfo;
import de.benjaminborbe.worktime.gui.guice.WorktimeGuiModules;
import de.benjaminborbe.worktime.gui.service.WorktimeGuiDashboardWidget;
import de.benjaminborbe.worktime.gui.servlet.WorktimeGuiServlet;

public class WorktimeGuiActivator extends HttpBundleActivator {

	@Inject
	private WorktimeGuiDashboardWidget worktimeDashboardWidget;

	@Inject
	private WorktimeGuiServlet worktimeServlet;

	public WorktimeGuiActivator() {
		super("worktime");
	}

	@Override
	protected Modules getModules(final BundleContext context) {
		return new WorktimeGuiModules(context);
	}

	@Override
	protected Collection<ServletInfo> getServletInfos() {
		final Set<ServletInfo> result = new HashSet<ServletInfo>(super.getServletInfos());
		result.add(new ServletInfo(worktimeServlet, "/"));
		return result;
	}

	@Override
	public Collection<ServiceInfo> getServiceInfos() {
		final Set<ServiceInfo> result = new HashSet<ServiceInfo>(super.getServiceInfos());
		result.add(new ServiceInfo(DashboardContentWidget.class, worktimeDashboardWidget, worktimeDashboardWidget.getClass().getName()));
		return result;
	}

}
