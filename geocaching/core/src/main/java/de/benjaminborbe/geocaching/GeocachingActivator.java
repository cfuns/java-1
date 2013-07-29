package de.benjaminborbe.geocaching;

import de.benjaminborbe.geocaching.api.GeocachingService;
import de.benjaminborbe.geocaching.guice.GeocachingModules;
import de.benjaminborbe.tools.guice.Modules;
import de.benjaminborbe.tools.osgi.BaseBundleActivator;
import de.benjaminborbe.tools.osgi.ServiceInfo;
import org.osgi.framework.BundleContext;

import javax.inject.Inject;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class GeocachingActivator extends BaseBundleActivator {

	@Inject
	private GeocachingService geocachingService;

	@Override
	protected Modules getModules(final BundleContext context) {
		return new GeocachingModules(context);
	}

	@Override
	public Collection<ServiceInfo> getServiceInfos() {
		final Set<ServiceInfo> result = new HashSet<ServiceInfo>(super.getServiceInfos());
		result.add(new ServiceInfo(GeocachingService.class, geocachingService));
		return result;
	}

}
