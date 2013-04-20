package de.benjaminborbe.search.gui;

import javax.inject.Inject;
import de.benjaminborbe.configuration.api.ConfigurationDescription;
import de.benjaminborbe.dashboard.api.DashboardContentWidget;
import de.benjaminborbe.navigation.api.NavigationEntry;
import de.benjaminborbe.search.api.SearchSpecial;
import de.benjaminborbe.search.api.SearchWidget;
import de.benjaminborbe.search.gui.config.SearchGuiConfig;
import de.benjaminborbe.search.gui.guice.SearchGuiModules;
import de.benjaminborbe.search.gui.service.SearchGuiDashboardWidget;
import de.benjaminborbe.search.gui.service.SearchGuiSpecialSearchRegistry;
import de.benjaminborbe.search.gui.service.SearchGuiSpecialSearchServiceTracker;
import de.benjaminborbe.search.gui.servlet.SearchGuiHelpServlet;
import de.benjaminborbe.search.gui.servlet.SearchGuiJsonServlet;
import de.benjaminborbe.search.gui.servlet.SearchGuiOsdServlet;
import de.benjaminborbe.search.gui.servlet.SearchGuiServiceComponentsServlet;
import de.benjaminborbe.search.gui.servlet.SearchGuiServlet;
import de.benjaminborbe.search.gui.servlet.SearchGuiSuggestServlet;
import de.benjaminborbe.search.gui.util.SearchGuiNavigationEntry;
import de.benjaminborbe.tools.guice.Modules;
import de.benjaminborbe.tools.osgi.HttpBundleActivator;
import de.benjaminborbe.tools.osgi.ResourceInfo;
import de.benjaminborbe.tools.osgi.ServiceInfo;
import de.benjaminborbe.tools.osgi.ServletInfo;
import org.osgi.framework.BundleContext;
import org.osgi.util.tracker.ServiceTracker;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class SearchGuiActivator extends HttpBundleActivator {

	@Inject
	private SearchGuiJsonServlet searchGuiJsonServlet;

	@Inject
	private SearchGuiOsdServlet searchOsdServlet;

	@Inject
	private SearchGuiServlet searchServlet;

	@Inject
	private SearchGuiServiceComponentsServlet searchServiceComponentsServlet;

	@Inject
	private SearchGuiSuggestServlet searchSuggestServlet;

	@Inject
	private SearchGuiDashboardWidget searchDashboardWidget;

	@Inject
	private SearchGuiSpecialSearchRegistry searchGuiSpecialSearchRegistry;

	@Inject
	private SearchWidget searchWidget;

	@Inject
	private SearchGuiHelpServlet searchHelpServlet;

	@Inject
	private SearchGuiNavigationEntry searchGuiNavigationEntry;

	@Inject
	private SearchGuiConfig searchGuiConfig;

	public SearchGuiActivator() {
		super(SearchGuiConstants.NAME);
	}

	@Override
	protected Modules getModules(final BundleContext context) {
		return new SearchGuiModules(context);
	}

	@Override
	protected Collection<ResourceInfo> getResouceInfos() {
		final Set<ResourceInfo> result = new HashSet<>(super.getResouceInfos());
		result.add(new ResourceInfo("/css", "css"));
		return result;
	}

	@Override
	protected Collection<ServletInfo> getServletInfos() {
		final Set<ServletInfo> result = new HashSet<>(super.getServletInfos());
		result.add(new ServletInfo(searchServlet, SearchGuiConstants.URL_SEARCH));
		result.add(new ServletInfo(searchGuiJsonServlet, SearchGuiConstants.URL_SEARCH_JSON));
		result.add(new ServletInfo(searchSuggestServlet, SearchGuiConstants.URL_SEARCH_SUGGEST));
		result.add(new ServletInfo(searchServiceComponentsServlet, SearchGuiConstants.URL_SEARCH_COMPONENT));
		result.add(new ServletInfo(searchOsdServlet, SearchGuiConstants.URL_OSD));
		result.add(new ServletInfo(searchHelpServlet, SearchGuiConstants.URL_SEARCH_HELP));
		return result;
	}

	@Override
	public Collection<ServiceInfo> getServiceInfos() {
		final Set<ServiceInfo> result = new HashSet<>(super.getServiceInfos());
		result.add(new ServiceInfo(DashboardContentWidget.class, searchDashboardWidget, searchDashboardWidget.getClass().getName()));
		result.add(new ServiceInfo(SearchWidget.class, searchWidget));
		result.add(new ServiceInfo(NavigationEntry.class, searchGuiNavigationEntry));
		for (final ConfigurationDescription configuration : searchGuiConfig.getConfigurations()) {
			result.add(new ServiceInfo(ConfigurationDescription.class, configuration, configuration.getName()));
		}
		return result;
	}

	@Override
	public Collection<ServiceTracker> getServiceTrackers(final BundleContext context) {
		final Set<ServiceTracker> serviceTrackers = new HashSet<>(super.getServiceTrackers(context));
		serviceTrackers.add(new SearchGuiSpecialSearchServiceTracker(searchGuiSpecialSearchRegistry, context, SearchSpecial.class));
		return serviceTrackers;
	}
}
