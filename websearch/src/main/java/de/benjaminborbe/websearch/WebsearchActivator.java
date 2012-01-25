package de.benjaminborbe.websearch;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.osgi.framework.BundleContext;
import org.osgi.util.tracker.ServiceTracker;

import com.google.inject.Inject;

import de.benjaminborbe.websearch.guice.WebsearchModules;
import de.benjaminborbe.websearch.service.WebsearchCrawlerNotify;
import de.benjaminborbe.websearch.service.WebsearchSearchServiceComponent;
import de.benjaminborbe.websearch.servlet.WebsearchServlet;
import de.benjaminborbe.crawler.api.CrawlerNotifier;
import de.benjaminborbe.search.api.SearchServiceComponent;
import de.benjaminborbe.tools.guice.Modules;
import de.benjaminborbe.tools.osgi.FilterInfo;
import de.benjaminborbe.tools.osgi.HttpBundleActivator;
import de.benjaminborbe.tools.osgi.ResourceInfo;
import de.benjaminborbe.tools.osgi.ServiceInfo;
import de.benjaminborbe.tools.osgi.ServletInfo;

public class WebsearchActivator extends HttpBundleActivator {

	public static final String INDEX = "websearch";

	@Inject
	private WebsearchServlet websearchServlet;

	@Inject
	private WebsearchCrawlerNotify websearchCrawlerNotify;

	@Inject
	private WebsearchSearchServiceComponent websearchSearchServiceComponent;

	public WebsearchActivator() {
		super("websearch");
	}

	@Override
	protected Modules getModules(final BundleContext context) {
		return new WebsearchModules(context);
	}

	@Override
	protected Collection<ServletInfo> getServletInfos() {
		final Set<ServletInfo> result = new HashSet<ServletInfo>(super.getServletInfos());
		result.add(new ServletInfo(websearchServlet, "/"));
		return result;
	}

	@Override
	protected Collection<FilterInfo> getFilterInfos() {
		final Set<FilterInfo> result = new HashSet<FilterInfo>(super.getFilterInfos());
		// result.add(new FilterInfo(websearchFilter, ".*", 1));
		return result;
	}

	@Override
	protected Collection<ResourceInfo> getResouceInfos() {
		final Set<ResourceInfo> result = new HashSet<ResourceInfo>(super.getResouceInfos());
		// result.add(new ResourceInfo("/css", "css"));
		// result.add(new ResourceInfo("/js", "js"));
		return result;
	}

	@Override
	protected Collection<ServiceInfo> getServiceInfos() {
		final Set<ServiceInfo> result = new HashSet<ServiceInfo>(super.getServiceInfos());
		result.add(new ServiceInfo(CrawlerNotifier.class, websearchCrawlerNotify));
		result.add(new ServiceInfo(SearchServiceComponent.class, websearchSearchServiceComponent, websearchSearchServiceComponent.getClass().getName()));
		return result;
	}

	@Override
	protected Collection<ServiceTracker> getServiceTrackers(final BundleContext context) {
		final Set<ServiceTracker> serviceTrackers = new HashSet<ServiceTracker>(super.getServiceTrackers(context));
		// serviceTrackers.add(new WebsearchServiceTracker(websearchRegistry, context,
		// WebsearchService.class));
		return serviceTrackers;
	}
}
