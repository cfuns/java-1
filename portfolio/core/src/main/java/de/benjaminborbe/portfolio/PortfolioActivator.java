package de.benjaminborbe.portfolio;

import javax.inject.Inject;
import de.benjaminborbe.portfolio.api.PortfolioService;
import de.benjaminborbe.portfolio.guice.PortfolioModules;
import de.benjaminborbe.tools.guice.Modules;
import de.benjaminborbe.tools.osgi.BaseBundleActivator;
import de.benjaminborbe.tools.osgi.ServiceInfo;
import org.osgi.framework.BundleContext;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class PortfolioActivator extends BaseBundleActivator {

	@Inject
	private PortfolioService portfolioService;

	@Override
	protected Modules getModules(final BundleContext context) {
		return new PortfolioModules(context);
	}

	@Override
	public Collection<ServiceInfo> getServiceInfos() {
		final Set<ServiceInfo> result = new HashSet<>(super.getServiceInfos());
		result.add(new ServiceInfo(PortfolioService.class, portfolioService));
		return result;
	}

}
