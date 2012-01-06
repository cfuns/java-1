package de.benjaminborbe.search;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.osgi.framework.BundleContext;
import org.osgi.util.tracker.ServiceTracker;

import com.google.inject.Inject;

import de.benjaminborbe.dashboard.api.DashboardContentWidget;
import de.benjaminborbe.search.api.SearchServiceComponent;
import de.benjaminborbe.search.api.SearchWidget;
import de.benjaminborbe.search.guice.SearchModules;
import de.benjaminborbe.search.service.SearchDashboardWidget;
import de.benjaminborbe.search.service.SearchServiceComponentServiceTracker;
import de.benjaminborbe.search.servlet.SearchOsdServlet;
import de.benjaminborbe.search.servlet.SearchServiceComponentsServlet;
import de.benjaminborbe.search.servlet.SearchServlet;
import de.benjaminborbe.search.servlet.SearchSuggestServlet;
import de.benjaminborbe.search.util.SearchServiceComponentRegistry;
import de.benjaminborbe.tools.guice.Modules;
import de.benjaminborbe.tools.osgi.HttpBundleActivator;
import de.benjaminborbe.tools.osgi.ResourceInfo;
import de.benjaminborbe.tools.osgi.ServiceInfo;
import de.benjaminborbe.tools.osgi.ServletInfo;

public class SearchActivator extends HttpBundleActivator {

	public SearchActivator() {
		super("search");
	}

	@Inject
	private SearchServiceComponentsServlet searchServiceComponentsServlet;

	@Inject
	private SearchWidget searchWidget;

	@Inject
	private SearchSuggestServlet searchSuggestServlet;

	@Inject
	private SearchServiceComponentRegistry searchServiceComponentRegistry;

	@Inject
	private SearchOsdServlet searchOsdServlet;

	@Inject
	private SearchDashboardWidget searchDashboardWidget;

	@Inject
	private SearchServlet searchServlet;

	@Override
	protected Collection<ServiceTracker> getServiceTrackers(final BundleContext context) {
		final Set<ServiceTracker> serviceTrackers = new HashSet<ServiceTracker>(super.getServiceTrackers(context));
		serviceTrackers.add(new SearchServiceComponentServiceTracker(searchServiceComponentRegistry, context, SearchServiceComponent.class));
		return serviceTrackers;
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
		result.add(new ServletInfo(searchServiceComponentsServlet, "/component"));
		result.add(new ServletInfo(searchOsdServlet, "/osd.xml"));
		return result;
	}

	@Override
	protected Modules getModules(final BundleContext context) {
		return new SearchModules(context);
	}

	@Override
	protected Collection<ServiceInfo> getServiceInfos() {
		final Set<ServiceInfo> result = new HashSet<ServiceInfo>(super.getServiceInfos());
		result.add(new ServiceInfo(DashboardContentWidget.class, searchDashboardWidget, searchDashboardWidget.getClass().getName()));
		result.add(new ServiceInfo(SearchWidget.class, searchWidget));
		return result;
	}
}
