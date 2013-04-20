package de.benjaminborbe.worktime.gui;

import javax.inject.Inject;
import de.benjaminborbe.dashboard.api.DashboardContentWidget;
import de.benjaminborbe.tools.guice.Modules;
import de.benjaminborbe.tools.osgi.HttpBundleActivator;
import de.benjaminborbe.tools.osgi.ServiceInfo;
import de.benjaminborbe.tools.osgi.ServletInfo;
import de.benjaminborbe.worktime.gui.guice.WorktimeGuiModules;
import de.benjaminborbe.worktime.gui.service.WorktimeGuiDashboardWidget;
import de.benjaminborbe.worktime.gui.servlet.WorktimeGuiInOfficeServlet;
import de.benjaminborbe.worktime.gui.servlet.WorktimeGuiServlet;
import org.osgi.framework.BundleContext;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class WorktimeGuiActivator extends HttpBundleActivator {

	@Inject
	private WorktimeGuiDashboardWidget worktimeDashboardWidget;

	@Inject
	private WorktimeGuiInOfficeServlet worktimeGuiInOfficeServlet;

	@Inject
	private WorktimeGuiServlet worktimeServlet;

	public WorktimeGuiActivator() {
		super(WorktimeGuiConstants.NAME);
	}

	@Override
	protected Modules getModules(final BundleContext context) {
		return new WorktimeGuiModules(context);
	}

	@Override
	protected Collection<ServletInfo> getServletInfos() {
		final Set<ServletInfo> result = new HashSet<>(super.getServletInfos());
		result.add(new ServletInfo(worktimeServlet, "/"));
		result.add(new ServletInfo(worktimeGuiInOfficeServlet, "/inOffice"));
		return result;
	}

	@Override
	public Collection<ServiceInfo> getServiceInfos() {
		final Set<ServiceInfo> result = new HashSet<>(super.getServiceInfos());
		result.add(new ServiceInfo(DashboardContentWidget.class, worktimeDashboardWidget, worktimeDashboardWidget.getClass().getName()));
		return result;
	}

}
