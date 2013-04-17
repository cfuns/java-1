package de.benjaminborbe.navigation.gui;

import com.google.inject.Inject;
import de.benjaminborbe.navigation.api.NavigationWidget;
import de.benjaminborbe.navigation.gui.guice.NavigationGuiModules;
import de.benjaminborbe.navigation.gui.servlet.NavigationGuiServlet;
import de.benjaminborbe.tools.guice.Modules;
import de.benjaminborbe.tools.osgi.HttpBundleActivator;
import de.benjaminborbe.tools.osgi.ServiceInfo;
import de.benjaminborbe.tools.osgi.ServletInfo;
import org.osgi.framework.BundleContext;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class NavigationGuiActivator extends HttpBundleActivator {

	@Inject
	private NavigationGuiServlet navigationGuiServlet;

	@Inject
	private NavigationWidget navigationWidget;

	public NavigationGuiActivator() {
		super(NavigationGuiConstants.NAME);
	}

	@Override
	protected Modules getModules(final BundleContext context) {
		return new NavigationGuiModules(context);
	}

	@Override
	protected Collection<ServletInfo> getServletInfos() {
		final Set<ServletInfo> result = new HashSet<>(super.getServletInfos());
		result.add(new ServletInfo(navigationGuiServlet, "/"));
		return result;
	}

	@Override
	public Collection<ServiceInfo> getServiceInfos() {
		final Set<ServiceInfo> result = new HashSet<>(super.getServiceInfos());
		result.add(new ServiceInfo(NavigationWidget.class, navigationWidget));
		return result;
	}

}
