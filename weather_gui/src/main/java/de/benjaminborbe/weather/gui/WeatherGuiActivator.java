package de.benjaminborbe.weather.gui;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.osgi.framework.BundleContext;

import com.google.inject.Inject;

import de.benjaminborbe.weather.gui.guice.WeatherGuiModules;
import de.benjaminborbe.weather.gui.servlet.WeatherGuiServlet;
import de.benjaminborbe.tools.guice.Modules;
import de.benjaminborbe.tools.osgi.HttpBundleActivator;
import de.benjaminborbe.tools.osgi.ServletInfo;

public class WeatherGuiActivator extends HttpBundleActivator {

	@Inject
	private WeatherGuiServlet weatherGuiServlet;

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
		result.add(new ServletInfo(weatherGuiServlet, "/"));
		return result;
	}

}
