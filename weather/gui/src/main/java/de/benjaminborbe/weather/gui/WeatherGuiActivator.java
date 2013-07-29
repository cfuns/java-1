package de.benjaminborbe.weather.gui;

import de.benjaminborbe.dashboard.api.DashboardContentWidget;
import de.benjaminborbe.tools.guice.Modules;
import de.benjaminborbe.tools.osgi.HttpBundleActivator;
import de.benjaminborbe.tools.osgi.ServiceInfo;
import de.benjaminborbe.tools.osgi.ServletInfo;
import de.benjaminborbe.weather.gui.guice.WeatherGuiModules;
import de.benjaminborbe.weather.gui.service.WeatherGuiDashboardWidget;
import de.benjaminborbe.weather.gui.servlet.WeatherGuiServlet;
import org.osgi.framework.BundleContext;

import javax.inject.Inject;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class WeatherGuiActivator extends HttpBundleActivator {

	@Inject
	private WeatherGuiServlet weatherServlet;

	@Inject
	private WeatherGuiDashboardWidget weatherDashboardWidget;

	public WeatherGuiActivator() {
		super(WeatherGuiConstants.NAME);
	}

	@Override
	protected Modules getModules(final BundleContext context) {
		return new WeatherGuiModules(context);
	}

	@Override
	protected Collection<ServletInfo> getServletInfos() {
		final Set<ServletInfo> result = new HashSet<ServletInfo>(super.getServletInfos());
		result.add(new ServletInfo(weatherServlet, "/"));
		return result;
	}

	@Override
	public Collection<ServiceInfo> getServiceInfos() {
		final Set<ServiceInfo> result = new HashSet<ServiceInfo>(super.getServiceInfos());
		result.add(new ServiceInfo(DashboardContentWidget.class, weatherDashboardWidget, weatherDashboardWidget.getClass().getName()));
		return result;
	}
}
