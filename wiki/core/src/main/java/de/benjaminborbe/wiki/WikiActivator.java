package de.benjaminborbe.wiki;

import javax.inject.Inject;
import de.benjaminborbe.tools.guice.Modules;
import de.benjaminborbe.tools.osgi.BaseBundleActivator;
import de.benjaminborbe.tools.osgi.ServiceInfo;
import de.benjaminborbe.wiki.api.WikiService;
import de.benjaminborbe.wiki.guice.WikiModules;
import org.osgi.framework.BundleContext;
import org.osgi.util.tracker.ServiceTracker;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class WikiActivator extends BaseBundleActivator {

	@Inject
	private WikiService wikiService;

	@Override
	protected Modules getModules(final BundleContext context) {
		return new WikiModules(context);
	}

	@Override
	public Collection<ServiceInfo> getServiceInfos() {
		final Set<ServiceInfo> result = new HashSet<>(super.getServiceInfos());
		result.add(new ServiceInfo(WikiService.class, wikiService));
		return result;
	}

	@Override
	public Collection<ServiceTracker> getServiceTrackers(final BundleContext context) {
		final Set<ServiceTracker> serviceTrackers = new HashSet<>(super.getServiceTrackers(context));
		// serviceTrackers.add(new WikiServiceTracker(wikiRegistry, context,
		// WikiService.class));
		return serviceTrackers;
	}
}
