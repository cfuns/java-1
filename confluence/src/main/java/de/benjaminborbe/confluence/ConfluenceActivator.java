package de.benjaminborbe.confluence;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.osgi.framework.BundleContext;
import org.osgi.util.tracker.ServiceTracker;

import com.google.inject.Inject;

import de.benjaminborbe.confluence.api.ConfluenceService;
import de.benjaminborbe.confluence.guice.ConfluenceModules;
import de.benjaminborbe.confluence.search.ConfluenceSearchServiceComponent;
import de.benjaminborbe.search.api.SearchServiceComponent;
import de.benjaminborbe.tools.guice.Modules;
import de.benjaminborbe.tools.osgi.BaseBundleActivator;
import de.benjaminborbe.tools.osgi.ServiceInfo;

public class ConfluenceActivator extends BaseBundleActivator {

	@Inject
	private ConfluenceService confluenceService;

	@Inject
	private ConfluenceSearchServiceComponent confluenceSearchServiceComponent;

	@Override
	protected Modules getModules(final BundleContext context) {
		return new ConfluenceModules(context);
	}

	@Override
	public Collection<ServiceInfo> getServiceInfos() {
		final Set<ServiceInfo> result = new HashSet<ServiceInfo>(super.getServiceInfos());
		result.add(new ServiceInfo(ConfluenceService.class, confluenceService));
		result.add(new ServiceInfo(SearchServiceComponent.class, confluenceSearchServiceComponent, confluenceSearchServiceComponent.getClass().getName()));
		return result;
	}

	@Override
	public Collection<ServiceTracker> getServiceTrackers(final BundleContext context) {
		final Set<ServiceTracker> serviceTrackers = new HashSet<ServiceTracker>(super.getServiceTrackers(context));
		// serviceTrackers.add(new ConfluenceServiceTracker(confluenceRegistry, context,
		// ConfluenceService.class));
		return serviceTrackers;
	}
}
