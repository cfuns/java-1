package de.benjaminborbe.crawler;

import de.benjaminborbe.crawler.api.CrawlerNotifier;
import de.benjaminborbe.crawler.api.CrawlerService;
import de.benjaminborbe.crawler.guice.CrawlerModules;
import de.benjaminborbe.crawler.service.CrawlerMessageConsumer;
import de.benjaminborbe.crawler.service.CrawlerNotifierServiceTracker;
import de.benjaminborbe.crawler.util.CrawlerNotifierRegistry;
import de.benjaminborbe.message.api.MessageConsumer;
import de.benjaminborbe.tools.guice.Modules;
import de.benjaminborbe.tools.osgi.BaseBundleActivator;
import de.benjaminborbe.tools.osgi.ServiceInfo;
import org.osgi.framework.BundleContext;
import org.osgi.util.tracker.ServiceTracker;

import javax.inject.Inject;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class CrawlerActivator extends BaseBundleActivator {

	@Inject
	private CrawlerMessageConsumer crawlerMessageConsumer;

	@Inject
	private CrawlerService crawlerService;

	@Inject
	private CrawlerNotifierRegistry crawlerNotifierRegistry;

	@Override
	protected Modules getModules(final BundleContext context) {
		return new CrawlerModules(context);
	}

	@Override
	public Collection<ServiceInfo> getServiceInfos() {
		final Set<ServiceInfo> result = new HashSet<ServiceInfo>(super.getServiceInfos());
		result.add(new ServiceInfo(CrawlerService.class, crawlerService));
		result.add(new ServiceInfo(MessageConsumer.class, crawlerMessageConsumer));
		return result;
	}

	@Override
	public Collection<ServiceTracker> getServiceTrackers(final BundleContext context) {
		final Set<ServiceTracker> serviceTrackers = new HashSet<ServiceTracker>(super.getServiceTrackers(context));
		serviceTrackers.add(new CrawlerNotifierServiceTracker(crawlerNotifierRegistry, context, CrawlerNotifier.class));
		return serviceTrackers;
	}
}
