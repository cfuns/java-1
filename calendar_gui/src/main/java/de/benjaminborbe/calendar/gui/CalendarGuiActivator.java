package de.benjaminborbe.calendar.gui;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.osgi.framework.BundleContext;

import com.google.inject.Inject;

import de.benjaminborbe.calendar.gui.guice.CalendarGuiModules;
import de.benjaminborbe.calendar.gui.service.CalendarGuiDashboardWidget;
import de.benjaminborbe.calendar.gui.servlet.CalendarGuiServlet;
import de.benjaminborbe.dashboard.api.DashboardContentWidget;
import de.benjaminborbe.tools.guice.Modules;
import de.benjaminborbe.tools.osgi.HttpBundleActivator;
import de.benjaminborbe.tools.osgi.ServiceInfo;
import de.benjaminborbe.tools.osgi.ServletInfo;

public class CalendarGuiActivator extends HttpBundleActivator {

	@Inject
	private CalendarGuiServlet calendarGuiServlet;

	@Inject
	private CalendarGuiDashboardWidget calendarDashboardWidget;

	public CalendarGuiActivator() {
		super("calendar");
	}

	@Override
	protected Modules getModules(final BundleContext context) {
		return new CalendarGuiModules(context);
	}

	@Override
	protected Collection<ServletInfo> getServletInfos() {
		final Set<ServletInfo> result = new HashSet<ServletInfo>(super.getServletInfos());
		result.add(new ServletInfo(calendarGuiServlet, "/"));
		return result;
	}

	@Override
	protected Collection<ServiceInfo> getServiceInfos() {
		final Set<ServiceInfo> result = new HashSet<ServiceInfo>(super.getServiceInfos());
		result.add(new ServiceInfo(DashboardContentWidget.class, calendarDashboardWidget, calendarDashboardWidget.getClass().getName()));
		return result;
	}

}
