package de.benjaminborbe.websearch;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.osgi.framework.BundleContext;
import org.osgi.util.tracker.ServiceTracker;

import com.google.inject.Inject;

import de.benjaminborbe.crawler.api.CrawlerNotifier;
import de.benjaminborbe.cron.api.CronJob;
import de.benjaminborbe.search.api.SearchServiceComponent;
import de.benjaminborbe.tools.guice.Modules;
import de.benjaminborbe.tools.osgi.BaseBundleActivator;
import de.benjaminborbe.tools.osgi.ServiceInfo;
import de.benjaminborbe.websearch.api.WebsearchService;
import de.benjaminborbe.websearch.cron.RefreshPagesCronJob;
import de.benjaminborbe.websearch.guice.WebsearchModules;
import de.benjaminborbe.websearch.service.WebsearchCrawlerNotify;
import de.benjaminborbe.websearch.service.WebsearchSearchServiceComponent;

public class WebsearchActivator extends BaseBundleActivator {

	public static final String INDEX = "websearch";

	@Inject
	private WebsearchCrawlerNotify websearchCrawlerNotify;

	@Inject
	private WebsearchSearchServiceComponent websearchSearchServiceComponent;

	@Inject
	private RefreshPagesCronJob refreshPagesCronJob;

	@Inject
	private WebsearchService websearchService;

	@Override
	protected Modules getModules(final BundleContext context) {
		return new WebsearchModules(context);
	}

	@Override
	public Collection<ServiceInfo> getServiceInfos() {
		final Set<ServiceInfo> result = new HashSet<ServiceInfo>(super.getServiceInfos());
		result.add(new ServiceInfo(CrawlerNotifier.class, websearchCrawlerNotify));
		result.add(new ServiceInfo(SearchServiceComponent.class, websearchSearchServiceComponent, websearchSearchServiceComponent.getClass().getName()));
		result.add(new ServiceInfo(CronJob.class, refreshPagesCronJob, refreshPagesCronJob.getClass().getName()));
		result.add(new ServiceInfo(WebsearchService.class, websearchService));
		return result;
	}

	@Override
	public Collection<ServiceTracker> getServiceTrackers(final BundleContext context) {
		final Set<ServiceTracker> serviceTrackers = new HashSet<ServiceTracker>(super.getServiceTrackers(context));
		// serviceTrackers.add(new WebsearchServiceTracker(websearchRegistry, context,
		// WebsearchService.class));
		return serviceTrackers;
	}
}
