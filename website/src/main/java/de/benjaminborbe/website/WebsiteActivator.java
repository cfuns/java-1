package de.benjaminborbe.website;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.osgi.framework.BundleContext;
import org.osgi.util.tracker.ServiceTracker;

import com.google.inject.Inject;

import de.benjaminborbe.navigation.api.NavigationEntry;
import de.benjaminborbe.navigation.api.NavigationEntryImpl;
import de.benjaminborbe.tools.guice.Modules;
import de.benjaminborbe.tools.osgi.FilterInfo;
import de.benjaminborbe.tools.osgi.HttpBundleActivator;
import de.benjaminborbe.tools.osgi.ResourceInfo;
import de.benjaminborbe.tools.osgi.ServiceInfo;
import de.benjaminborbe.tools.osgi.ServletInfo;
import de.benjaminborbe.website.guice.WebsiteModules;
import de.benjaminborbe.website.servlet.WebsiteDashboardServlet;
import de.benjaminborbe.website.servlet.WebsiteSearchServlet;

public class WebsiteActivator extends HttpBundleActivator {

	@Inject
	private WebsiteDashboardServlet websiteDashboardServlet;

	@Inject
	private WebsiteSearchServlet websiteSearchServlet;

	public WebsiteActivator() {
		super("website");
	}

	@Override
	protected Modules getModules(final BundleContext context) {
		return new WebsiteModules(context);
	}

	@Override
	protected Collection<ServletInfo> getServletInfos() {
		final Set<ServletInfo> result = new HashSet<ServletInfo>(super.getServletInfos());
		result.add(new ServletInfo(websiteDashboardServlet, "/dashboard"));
		result.add(new ServletInfo(websiteSearchServlet, "/search"));
		return result;
	}

	@Override
	protected Collection<FilterInfo> getFilterInfos() {
		final Set<FilterInfo> result = new HashSet<FilterInfo>(super.getFilterInfos());
		// result.add(new FilterInfo(websiteFilter, ".*", 1));
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
		// result.add(new ServiceInfo(WebsiteService.class, websiteService));
		result.add(buildNavigationEntry("/bb/website/dashboard", "Dashboard"));
		return result;
	}

	private ServiceInfo buildNavigationEntry(final String url, final String title) {
		final NavigationEntry navigationEntry = new NavigationEntryImpl(title, url);
		return new ServiceInfo(NavigationEntry.class, navigationEntry, title);
	}

	@Override
	protected Collection<ServiceTracker> getServiceTrackers(final BundleContext context) {
		final Set<ServiceTracker> serviceTrackers = new HashSet<ServiceTracker>(super.getServiceTrackers(context));
		// serviceTrackers.add(new WebsiteServiceTracker(websiteRegistry, context,
		// WebsiteService.class));
		return serviceTrackers;
	}
}
