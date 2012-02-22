package de.benjaminborbe.navigation.gui;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.osgi.framework.BundleContext;

import com.google.inject.Inject;

import de.benjaminborbe.navigation.api.NavigationWidget;
import de.benjaminborbe.navigation.gui.guice.NavigationGuiModules;
import de.benjaminborbe.navigation.gui.servlet.NavigationGuiServlet;
import de.benjaminborbe.tools.guice.Modules;
import de.benjaminborbe.tools.osgi.HttpBundleActivator;
import de.benjaminborbe.tools.osgi.ServiceInfo;
import de.benjaminborbe.tools.osgi.ServletInfo;

public class NavigationGuiActivator extends HttpBundleActivator {

	@Inject
	private NavigationGuiServlet navigationGuiServlet;

	@Inject
	private NavigationWidget navigationWidget;

	public NavigationGuiActivator() {
		super("navigation");
	}

	@Override
	protected Modules getModules(final BundleContext context) {
		return new NavigationGuiModules(context);
	}

	@Override
	protected Collection<ServletInfo> getServletInfos() {
		final Set<ServletInfo> result = new HashSet<ServletInfo>(super.getServletInfos());
		result.add(new ServletInfo(navigationGuiServlet, "/"));
		return result;
	}

	@Override
	public Collection<ServiceInfo> getServiceInfos() {
		final Set<ServiceInfo> result = new HashSet<ServiceInfo>(super.getServiceInfos());
		result.add(new ServiceInfo(NavigationWidget.class, navigationWidget));
		return result;
	}

}
