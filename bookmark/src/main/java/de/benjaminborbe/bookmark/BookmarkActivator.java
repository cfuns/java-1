package de.benjaminborbe.bookmark;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.osgi.framework.BundleContext;

import com.google.inject.Inject;

import de.benjaminborbe.bookmark.api.BookmarkService;
import de.benjaminborbe.bookmark.guice.BookmarkModules;
import de.benjaminborbe.bookmark.service.BookmarkSearchServiceComponentImpl;
import de.benjaminborbe.search.api.SearchServiceComponent;
import de.benjaminborbe.tools.guice.Modules;
import de.benjaminborbe.tools.osgi.BaseBundleActivator;
import de.benjaminborbe.tools.osgi.ServiceInfo;

public class BookmarkActivator extends BaseBundleActivator {

	@Inject
	private BookmarkService bookmarkService;

	@Inject
	private BookmarkSearchServiceComponentImpl bookmarkSearchService;

	@Override
	protected Modules getModules(final BundleContext context) {
		return new BookmarkModules(context);
	}

	@Override
	protected Collection<ServiceInfo> getServiceInfos() {
		final Set<ServiceInfo> result = new HashSet<ServiceInfo>(super.getServiceInfos());
		result.add(new ServiceInfo(BookmarkService.class, bookmarkService));
		result.add(new ServiceInfo(SearchServiceComponent.class, bookmarkSearchService, bookmarkSearchService.getClass().getName()));
		return result;
	}
}
