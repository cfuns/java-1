package de.benjaminborbe.wiki;

import de.benjaminborbe.tools.guice.Modules;
import de.benjaminborbe.tools.osgi.BaseBundleActivator;
import de.benjaminborbe.tools.osgi.ServiceInfo;
import de.benjaminborbe.wiki.api.WikiService;
import de.benjaminborbe.wiki.guice.WikiModules;
import org.osgi.framework.BundleContext;

import javax.inject.Inject;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class WikiActivator extends BaseBundleActivator {

	@Inject
	private WikiService wikiService;

	@Override
	protected Modules getModules(final BundleContext context) {
		return new WikiModules(context);
	}

	@Override
	public Collection<ServiceInfo> getServiceInfos() {
		final Set<ServiceInfo> result = new HashSet<ServiceInfo>(super.getServiceInfos());
		result.add(new ServiceInfo(WikiService.class, wikiService));
		return result;
	}

}
