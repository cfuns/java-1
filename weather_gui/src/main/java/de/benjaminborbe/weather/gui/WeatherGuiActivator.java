package de.benjaminborbe.weather.gui;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.osgi.framework.BundleContext;

import com.google.inject.Inject;

import de.benjaminborbe.weather.gui.guice.WeatherGuiModules;
import de.benjaminborbe.weather.gui.service.WeatherGuiDashboardWidget;
import de.benjaminborbe.weather.gui.servlet.WeatherGuiServlet;
import de.benjaminborbe.dashboard.api.DashboardContentWidget;
import de.benjaminborbe.tools.guice.Modules;
import de.benjaminborbe.tools.osgi.FilterInfo;
import de.benjaminborbe.tools.osgi.HttpBundleActivator;
import de.benjaminborbe.tools.osgi.ResourceInfo;
import de.benjaminborbe.tools.osgi.ServiceInfo;
import de.benjaminborbe.tools.osgi.ServletInfo;

public class WeatherGuiActivator extends HttpBundleActivator {

	@Inject
	private WeatherGuiServlet weatherServlet;

	@Inject
	private WeatherGuiDashboardWidget weatherDashboardWidget;

	public WeatherGuiActivator() {
		super("weather");
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
	protected Collection<FilterInfo> getFilterInfos() {
		final Set<FilterInfo> result = new HashSet<FilterInfo>(super.getFilterInfos());
		// result.add(new FilterInfo(weatherFilter, ".*", 1));
		return result;
	}

	@Override
	protected Collection<ResourceInfo> getResouceInfos() {
		final Set<ResourceInfo> result = new HashSet<ResourceInfo>(super.getResouceInfos());
		// result.add(new ResourceInfo("/css", "css"));
		return result;
	}

	@Override
	protected Collection<ServiceInfo> getServiceInfos() {
		final Set<ServiceInfo> result = new HashSet<ServiceInfo>(super.getServiceInfos());
		result.add(new ServiceInfo(DashboardContentWidget.class, weatherDashboardWidget, weatherDashboardWidget.getClass().getName()));
		return result;
	}
}
