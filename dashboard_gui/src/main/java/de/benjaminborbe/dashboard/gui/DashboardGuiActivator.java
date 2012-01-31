package de.benjaminborbe.dashboard.gui;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.osgi.framework.BundleContext;

import com.google.inject.Inject;

import de.benjaminborbe.dashboard.gui.guice.DashboardGuiModules;
import de.benjaminborbe.dashboard.gui.servlet.DashboardGuiServlet;
import de.benjaminborbe.tools.guice.Modules;
import de.benjaminborbe.tools.osgi.HttpBundleActivator;
import de.benjaminborbe.tools.osgi.ServletInfo;

public class DashboardGuiActivator extends HttpBundleActivator {

	@Inject
	private DashboardGuiServlet dashboardGuiServlet;

	public DashboardGuiActivator() {
		super("dashboard");
	}

	@Override
	protected Modules getModules(final BundleContext context) {
		return new DashboardGuiModules(context);
	}

	@Override
	protected Collection<ServletInfo> getServletInfos() {
		final Set<ServletInfo> result = new HashSet<ServletInfo>(super.getServletInfos());
		result.add(new ServletInfo(dashboardGuiServlet, "/"));
		return result;
	}

}
