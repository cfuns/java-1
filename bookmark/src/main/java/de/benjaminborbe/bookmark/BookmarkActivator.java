package de.benjaminborbe.bookmark;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.osgi.framework.BundleContext;

import com.google.inject.Inject;

import de.benjaminborbe.bookmark.api.BookmarkService;
import de.benjaminborbe.bookmark.guice.BookmarkModules;
import de.benjaminborbe.bookmark.service.BookmarkFavoriteDashboardWidget;
import de.benjaminborbe.bookmark.service.BookmarkSearchServiceComponentImpl;
import de.benjaminborbe.bookmark.servlet.BookmarkServlet;
import de.benjaminborbe.dashboard.api.DashboardContentWidget;
import de.benjaminborbe.search.api.SearchServiceComponent;
import de.benjaminborbe.tools.guice.Modules;
import de.benjaminborbe.tools.osgi.HttpBundleActivator;
import de.benjaminborbe.tools.osgi.ServiceInfo;
import de.benjaminborbe.tools.osgi.ServletInfo;

public class BookmarkActivator extends HttpBundleActivator {

	@Inject
	private BookmarkServlet bookmarkServlet;

	@Inject
	private BookmarkService bookmarkService;

	@Inject
	private BookmarkSearchServiceComponentImpl bookmarkSearchService;

	@Inject
	private BookmarkFavoriteDashboardWidget bookmarkFavoriteDashboardWidget;

	public BookmarkActivator() {
		super("bookmark");
	}

	@Override
	protected Modules getModules(final BundleContext context) {
		return new BookmarkModules(context);
	}

	@Override
	protected Collection<ServiceInfo> getServiceInfos() {
		final Set<ServiceInfo> result = new HashSet<ServiceInfo>(super.getServiceInfos());
		result.add(new ServiceInfo(BookmarkService.class, bookmarkService));
		result.add(new ServiceInfo(SearchServiceComponent.class, bookmarkSearchService, bookmarkSearchService.getClass().getName()));
		result.add(new ServiceInfo(DashboardContentWidget.class, bookmarkFavoriteDashboardWidget, bookmarkFavoriteDashboardWidget.getClass().getName()));
		return result;
	}

	@Override
	protected Collection<ServletInfo> getServletInfos() {
		final Set<ServletInfo> result = new HashSet<ServletInfo>(super.getServletInfos());
		result.add(new ServletInfo(bookmarkServlet, "/"));
		return result;
	}

}
