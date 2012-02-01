package de.benjaminborbe.crawler;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.osgi.framework.BundleContext;
import org.osgi.util.tracker.ServiceTracker;

import com.google.inject.Inject;

import de.benjaminborbe.crawler.api.CrawlerNotifier;
import de.benjaminborbe.crawler.api.CrawlerService;
import de.benjaminborbe.crawler.guice.CrawlerModules;
import de.benjaminborbe.crawler.service.CrawlerNotifierServiceTracker;
import de.benjaminborbe.crawler.util.CrawlerNotifierRegistry;
import de.benjaminborbe.tools.guice.Modules;
import de.benjaminborbe.tools.osgi.BaseBundleActivator;
import de.benjaminborbe.tools.osgi.ServiceInfo;

public class CrawlerActivator extends BaseBundleActivator {

	@Inject
	private CrawlerService crawlerService;

	@Inject
	private CrawlerNotifierRegistry crawlerNotifierRegistry;

	@Override
	protected Modules getModules(final BundleContext context) {
		return new CrawlerModules(context);
	}

	@Override
	protected Collection<ServiceInfo> getServiceInfos() {
		final Set<ServiceInfo> result = new HashSet<ServiceInfo>(super.getServiceInfos());
		result.add(new ServiceInfo(CrawlerService.class, crawlerService));
		return result;
	}

	@Override
	protected Collection<ServiceTracker> getServiceTrackers(final BundleContext context) {
		final Set<ServiceTracker> serviceTrackers = new HashSet<ServiceTracker>(super.getServiceTrackers(context));
		serviceTrackers.add(new CrawlerNotifierServiceTracker(crawlerNotifierRegistry, context, CrawlerNotifier.class));
		return serviceTrackers;
	}
}
