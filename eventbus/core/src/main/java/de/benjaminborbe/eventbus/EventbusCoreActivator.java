package de.benjaminborbe.eventbus;

import de.benjaminborbe.eventbus.api.EventbusService;
import de.benjaminborbe.eventbus.guice.EventbusModules;
import de.benjaminborbe.tools.guice.Modules;
import de.benjaminborbe.tools.osgi.BaseBundleActivator;
import de.benjaminborbe.tools.osgi.ServiceInfo;
import org.osgi.framework.BundleContext;
import org.osgi.util.tracker.ServiceTracker;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class EventbusCoreActivator extends BaseBundleActivator {

	@Inject
	private Logger logger;

	@Inject
	private EventbusService EventbusService;

	@Override
	protected Modules getModules(final BundleContext context) {
		return new EventbusModules(context);
	}

	@Override
	public Collection<ServiceInfo> getServiceInfos() {
		final Set<ServiceInfo> result = new HashSet<ServiceInfo>(super.getServiceInfos());
		result.add(new ServiceInfo(EventbusService.class, EventbusService));
		return result;
	}

	@Override
	public Collection<ServiceTracker> getServiceTrackers(final BundleContext context) {
		final Set<ServiceTracker> serviceTrackers = new HashSet<ServiceTracker>(super.getServiceTrackers(context));
		serviceTrackers.add(new EventbusServiceTracker(logger, context));
		return serviceTrackers;
	}

}
