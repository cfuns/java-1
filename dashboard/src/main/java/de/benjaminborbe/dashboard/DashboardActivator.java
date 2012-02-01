package de.benjaminborbe.dashboard;

import org.osgi.framework.BundleContext;
import de.benjaminborbe.dashboard.guice.DashboardModules;
import de.benjaminborbe.tools.guice.Modules;
import de.benjaminborbe.tools.osgi.BaseBundleActivator;

public class DashboardActivator extends BaseBundleActivator {

	@Override
	protected Modules getModules(final BundleContext context) {
		return new DashboardModules(context);
	}

}
