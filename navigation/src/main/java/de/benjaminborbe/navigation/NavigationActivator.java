package de.benjaminborbe.navigation;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.osgi.framework.BundleContext;

import com.google.inject.Inject;

import de.benjaminborbe.navigation.guice.NavigationModules;
import de.benjaminborbe.navigation.servlet.NavigationServlet;
import de.benjaminborbe.tools.guice.Modules;
import de.benjaminborbe.tools.osgi.HttpBundleActivator;
import de.benjaminborbe.tools.osgi.ServletInfo;

public class NavigationActivator extends HttpBundleActivator {

	@Inject
	private NavigationServlet navigationServlet;

	public NavigationActivator() {
		super("navigation");
	}

	@Override
	protected Modules getModules(final BundleContext context) {
		return new NavigationModules(context);
	}

	@Override
	protected Collection<ServletInfo> getServletInfos() {
		final Set<ServletInfo> result = new HashSet<ServletInfo>(super.getServletInfos());
		result.add(new ServletInfo(navigationServlet, "/"));
		return result;
	}

}
