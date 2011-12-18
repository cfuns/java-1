package de.benjaminborbe.search;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTracker;
import com.google.inject.Inject;
import de.benjaminborbe.search.api.SearchServiceComponent;
import de.benjaminborbe.search.guice.SearchModules;
import de.benjaminborbe.search.servlet.SearchServlet;
import de.benjaminborbe.search.servlet.SearchSuggestServlet;
import de.benjaminborbe.search.util.SearchServiceComponentRegistry;
import de.benjaminborbe.tools.guice.Modules;
import de.benjaminborbe.tools.osgi.HttpBundleActivator;
import de.benjaminborbe.tools.osgi.ResourceInfo;
import de.benjaminborbe.tools.osgi.ServletInfo;

public class SearchActivator extends HttpBundleActivator {

	public SearchActivator() {
		super("search");
	}

	@Inject
	private SearchServlet searchServlet;

	@Inject
	private SearchSuggestServlet searchSuggestServlet;

	@Inject
	private SearchServiceComponentRegistry searchServiceComponentRegistry;

	@Override
	protected Collection<ServiceTracker> getServiceTrackers(final BundleContext context) {
		final Set<ServiceTracker> serviceTrackers = new HashSet<ServiceTracker>(super.getServiceTrackers(context));
		// create serviceTracker for CronJob
		{
			final ServiceTracker serviceTracker = new ServiceTracker(context, SearchServiceComponent.class.getName(), null) {

				@Override
				public Object addingService(final ServiceReference ref) {
					final Object service = super.addingService(ref);
					serviceAdded((SearchServiceComponent) service);
					return service;
				}

				@Override
				public void removedService(final ServiceReference ref, final Object service) {
					serviceRemoved((SearchServiceComponent) service);
					super.removedService(ref, service);
				}
			};
			serviceTrackers.add(serviceTracker);
		}
		return serviceTrackers;
	}

	protected void serviceRemoved(final SearchServiceComponent service) {
		logger.debug("serviceRemoved: " + service);
		searchServiceComponentRegistry.unregister(service);
	}

	protected void serviceAdded(final SearchServiceComponent service) {
		logger.debug("serviceAdded: " + service);
		searchServiceComponentRegistry.register(service);
	}

	@Override
	protected Collection<ResourceInfo> getResouceInfos() {
		final Set<ResourceInfo> result = new HashSet<ResourceInfo>(super.getResouceInfos());
		result.add(new ResourceInfo("/css", "css"));
		result.add(new ResourceInfo("/js", "js"));
		return result;
	}

	@Override
	protected Collection<ServletInfo> getServletInfos() {
		final Set<ServletInfo> result = new HashSet<ServletInfo>(super.getServletInfos());
		result.add(new ServletInfo(searchServlet, "/"));
		result.add(new ServletInfo(searchSuggestServlet, "/suggest"));
		return result;
	}

	@Override
	protected Modules getModules(final BundleContext context) {
		return new SearchModules(context);
	}
}
