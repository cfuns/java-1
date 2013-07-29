package de.benjaminborbe.dashboard;

import de.benjaminborbe.dashboard.api.DashboardService;
import de.benjaminborbe.dashboard.guice.DashboardModules;
import de.benjaminborbe.tools.guice.Modules;
import de.benjaminborbe.tools.osgi.BaseBundleActivator;
import de.benjaminborbe.tools.osgi.ServiceInfo;
import org.osgi.framework.BundleContext;

import javax.inject.Inject;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class DashboardActivator extends BaseBundleActivator {

	@Inject
	private DashboardService dashboardService;

	@Override
	protected Modules getModules(final BundleContext context) {
		return new DashboardModules(context);
	}

	@Override
	public Collection<ServiceInfo> getServiceInfos() {
		final Set<ServiceInfo> result = new HashSet<ServiceInfo>(super.getServiceInfos());
		result.add(new ServiceInfo(DashboardService.class, dashboardService));
		return result;
	}

}
