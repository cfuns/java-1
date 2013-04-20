package de.benjaminborbe.calendar.gui;

import javax.inject.Inject;
import de.benjaminborbe.calendar.gui.guice.CalendarGuiModules;
import de.benjaminborbe.calendar.gui.service.CalendarGuiDashboardWidget;
import de.benjaminborbe.calendar.gui.servlet.CalendarGuiServlet;
import de.benjaminborbe.dashboard.api.DashboardContentWidget;
import de.benjaminborbe.tools.guice.Modules;
import de.benjaminborbe.tools.osgi.HttpBundleActivator;
import de.benjaminborbe.tools.osgi.ServiceInfo;
import de.benjaminborbe.tools.osgi.ServletInfo;
import org.osgi.framework.BundleContext;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class CalendarGuiActivator extends HttpBundleActivator {

	@Inject
	private CalendarGuiServlet calendarGuiServlet;

	@Inject
	private CalendarGuiDashboardWidget calendarDashboardWidget;

	public CalendarGuiActivator() {
		super(CalendarGuiConstants.NAME);
	}

	@Override
	protected Modules getModules(final BundleContext context) {
		return new CalendarGuiModules(context);
	}

	@Override
	protected Collection<ServletInfo> getServletInfos() {
		final Set<ServletInfo> result = new HashSet<>(super.getServletInfos());
		result.add(new ServletInfo(calendarGuiServlet, "/"));
		return result;
	}

	@Override
	public Collection<ServiceInfo> getServiceInfos() {
		final Set<ServiceInfo> result = new HashSet<>(super.getServiceInfos());
		result.add(new ServiceInfo(DashboardContentWidget.class, calendarDashboardWidget, calendarDashboardWidget.getClass().getName()));
		return result;
	}

}
