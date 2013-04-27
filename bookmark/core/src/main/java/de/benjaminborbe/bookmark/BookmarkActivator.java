package de.benjaminborbe.bookmark;

import de.benjaminborbe.bookmark.api.BookmarkService;
import de.benjaminborbe.bookmark.guice.BookmarkModules;
import de.benjaminborbe.bookmark.service.BookmarkSearchServiceComponent;
import de.benjaminborbe.bookmark.service.MyExtHttpService;
import de.benjaminborbe.bookmark.service.RegisteredServletSearchServiceComponent;
import de.benjaminborbe.search.api.SearchServiceComponent;
import de.benjaminborbe.tools.guice.Modules;
import de.benjaminborbe.tools.osgi.BaseBundleActivator;
import de.benjaminborbe.tools.osgi.ServiceInfo;
import org.apache.felix.http.api.ExtHttpService;
import org.osgi.framework.BundleContext;

import javax.inject.Inject;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class BookmarkActivator extends BaseBundleActivator {

	@Inject
	private BookmarkService bookmarkService;

	@Inject
	private BookmarkSearchServiceComponent bookmarkSearchService;

	@Inject
	private MyExtHttpService myExtHttpService;

	@Inject
	private RegisteredServletSearchServiceComponent registeredServletSearchServiceComponent;

	@Override
	protected Modules getModules(final BundleContext context) {
		return new BookmarkModules(context);
	}

	@Override
	public Collection<ServiceInfo> getServiceInfos() {
		final Set<ServiceInfo> result = new HashSet<>(super.getServiceInfos());
		result.add(new ServiceInfo(BookmarkService.class, bookmarkService));
		result.add(new ServiceInfo(ExtHttpService.class, myExtHttpService));
		result.add(new ServiceInfo(SearchServiceComponent.class, bookmarkSearchService, bookmarkSearchService.getClass().getName()));
		result.add(new ServiceInfo(SearchServiceComponent.class, registeredServletSearchServiceComponent, registeredServletSearchServiceComponent.getClass().getName()));
		return result;
	}
}
