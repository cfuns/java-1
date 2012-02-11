package de.benjaminborbe.eventbus;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.osgi.framework.BundleContext;

import com.google.inject.Inject;

import de.benjaminborbe.eventbus.api.EventbusService;
import de.benjaminborbe.eventbus.guice.EventbusModules;
import de.benjaminborbe.tools.guice.Modules;
import de.benjaminborbe.tools.osgi.BaseBundleActivator;
import de.benjaminborbe.tools.osgi.ServiceInfo;

public class EventbusActivator extends BaseBundleActivator {

	@Inject
	private EventbusService EventbusService;

	@Override
	protected Modules getModules(final BundleContext context) {
		return new EventbusModules(context);
	}

	@Override
	protected Collection<ServiceInfo> getServiceInfos() {
		final Set<ServiceInfo> result = new HashSet<ServiceInfo>(super.getServiceInfos());
		result.add(new ServiceInfo(EventbusService.class, EventbusService));
		return result;
	}
}
