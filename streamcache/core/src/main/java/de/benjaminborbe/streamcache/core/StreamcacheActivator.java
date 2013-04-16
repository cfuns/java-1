package de.benjaminborbe.streamcache.core;

import com.google.inject.Inject;
import de.benjaminborbe.streamcache.api.StreamcacheService;
import de.benjaminborbe.streamcache.core.guice.StreamcacheModules;
import de.benjaminborbe.tools.guice.Modules;
import de.benjaminborbe.tools.osgi.BaseBundleActivator;
import de.benjaminborbe.tools.osgi.ServiceInfo;
import org.osgi.framework.BundleContext;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class StreamcacheActivator extends BaseBundleActivator {

	@Inject
	private StreamcacheService streamcacheService;

	@Override
	protected Modules getModules(final BundleContext context) {
		return new StreamcacheModules(context);
	}

	@Override
	public Collection<ServiceInfo> getServiceInfos() {
		final Set<ServiceInfo> result = new HashSet<>(super.getServiceInfos());
		result.add(new ServiceInfo(StreamcacheService.class, streamcacheService));
		return result;
	}

}
