package de.benjaminborbe.websearch.core;

import com.google.inject.Inject;
import de.benjaminborbe.configuration.api.ConfigurationDescription;
import de.benjaminborbe.crawler.api.CrawlerNotifier;
import de.benjaminborbe.cron.api.CronJob;
import de.benjaminborbe.search.api.SearchServiceComponent;
import de.benjaminborbe.tools.guice.Modules;
import de.benjaminborbe.tools.osgi.BaseBundleActivator;
import de.benjaminborbe.tools.osgi.ServiceInfo;
import de.benjaminborbe.websearch.api.WebsearchService;
import de.benjaminborbe.websearch.core.config.WebsearchConfig;
import de.benjaminborbe.websearch.core.guice.WebsearchModules;
import de.benjaminborbe.websearch.core.service.WebsearchCrawlerNotify;
import de.benjaminborbe.websearch.core.service.WebsearchRefreshPagesCronJob;
import de.benjaminborbe.websearch.core.service.WebsearchSearchServiceComponent;
import org.osgi.framework.BundleContext;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class WebsearchActivator extends BaseBundleActivator {

	@Inject
	private WebsearchConfig websearchConfig;

	@Inject
	private WebsearchCrawlerNotify websearchCrawlerNotify;

	@Inject
	private WebsearchSearchServiceComponent websearchSearchServiceComponent;

	@Inject
	private WebsearchRefreshPagesCronJob refreshPagesCronJob;

	@Inject
	private WebsearchService websearchService;

	@Override
	protected Modules getModules(final BundleContext context) {
		return new WebsearchModules(context);
	}

	@Override
	public Collection<ServiceInfo> getServiceInfos() {
		final Set<ServiceInfo> result = new HashSet<>(super.getServiceInfos());
		result.add(new ServiceInfo(CrawlerNotifier.class, websearchCrawlerNotify));
		result.add(new ServiceInfo(SearchServiceComponent.class, websearchSearchServiceComponent, websearchSearchServiceComponent.getClass().getName()));
		result.add(new ServiceInfo(CronJob.class, refreshPagesCronJob, refreshPagesCronJob.getClass().getName()));
		result.add(new ServiceInfo(WebsearchService.class, websearchService));
		for (final ConfigurationDescription configuration : websearchConfig.getConfigurations()) {
			result.add(new ServiceInfo(ConfigurationDescription.class, configuration, configuration.getName()));
		}
		return result;
	}

}
