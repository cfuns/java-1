package de.benjaminborbe.googlesearch;

import javax.inject.Inject;
import de.benjaminborbe.googlesearch.api.GooglesearchService;
import de.benjaminborbe.googlesearch.guice.GooglesearchModules;
import de.benjaminborbe.googlesearch.service.GoogleSearchServiceComponent;
import de.benjaminborbe.search.api.SearchServiceComponent;
import de.benjaminborbe.tools.guice.Modules;
import de.benjaminborbe.tools.osgi.BaseBundleActivator;
import de.benjaminborbe.tools.osgi.ServiceInfo;
import org.osgi.framework.BundleContext;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class GooglesearchActivator extends BaseBundleActivator {

	@Inject
	private GooglesearchService googlesearchService;

	@Inject
	private GoogleSearchServiceComponent googleSearchServiceComponent;

	@Override
	protected Modules getModules(final BundleContext context) {
		return new GooglesearchModules(context);
	}

	@Override
	public Collection<ServiceInfo> getServiceInfos() {
		final Set<ServiceInfo> result = new HashSet<>(super.getServiceInfos());
		result.add(new ServiceInfo(GooglesearchService.class, googlesearchService));
		result.add(new ServiceInfo(SearchServiceComponent.class, googleSearchServiceComponent, googleSearchServiceComponent.getClass().getName()));
		return result;
	}

}
