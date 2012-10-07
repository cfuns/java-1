package de.benjaminborbe.portfolio;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.osgi.framework.BundleContext;
import org.osgi.util.tracker.ServiceTracker;

import com.google.inject.Inject;

import de.benjaminborbe.portfolio.api.PortfolioService;
import de.benjaminborbe.portfolio.guice.PortfolioModules;
import de.benjaminborbe.tools.guice.Modules;
import de.benjaminborbe.tools.osgi.BaseBundleActivator;
import de.benjaminborbe.tools.osgi.ServiceInfo;

public class PortfolioActivator extends BaseBundleActivator {

	@Inject
	private PortfolioService portfolioService;

	@Override
	protected Modules getModules(final BundleContext context) {
		return new PortfolioModules(context);
	}

	@Override
	public Collection<ServiceInfo> getServiceInfos() {
		final Set<ServiceInfo> result = new HashSet<ServiceInfo>(super.getServiceInfos());
		result.add(new ServiceInfo(PortfolioService.class, portfolioService));
		return result;
	}

	@Override
	public Collection<ServiceTracker> getServiceTrackers(final BundleContext context) {
		final Set<ServiceTracker> serviceTrackers = new HashSet<ServiceTracker>(super.getServiceTrackers(context));
		// serviceTrackers.add(new PortfolioServiceTracker(portfolioRegistry, context,
		// PortfolioService.class));
		return serviceTrackers;
	}
}
