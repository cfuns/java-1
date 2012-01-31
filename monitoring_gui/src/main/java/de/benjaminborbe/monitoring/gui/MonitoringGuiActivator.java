package de.benjaminborbe.monitoring.gui;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.osgi.framework.BundleContext;

import com.google.inject.Inject;

import de.benjaminborbe.monitoring.gui.guice.MonitoringGuiModules;
import de.benjaminborbe.monitoring.gui.servlet.MonitoringGuiServlet;
import de.benjaminborbe.tools.guice.Modules;
import de.benjaminborbe.tools.osgi.HttpBundleActivator;
import de.benjaminborbe.tools.osgi.ServletInfo;

public class MonitoringGuiActivator extends HttpBundleActivator {

	@Inject
	private MonitoringGuiServlet monitoringGuiServlet;

	public MonitoringGuiActivator() {
		super("monitoring");
	}

	@Override
	protected Modules getModules(final BundleContext context) {
		return new MonitoringGuiModules(context);
	}

	@Override
	protected Collection<ServletInfo> getServletInfos() {
		final Set<ServletInfo> result = new HashSet<ServletInfo>(super.getServletInfos());
		result.add(new ServletInfo(monitoringGuiServlet, "/"));
		return result;
	}

}
