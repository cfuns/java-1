package de.benjaminborbe.kiosk;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.osgi.framework.BundleContext;
import org.osgi.util.tracker.ServiceTracker;

import com.google.inject.Inject;

import de.benjaminborbe.kiosk.api.KioskService;
import de.benjaminborbe.kiosk.guice.KioskModules;
import de.benjaminborbe.kiosk.service.KioskBookingMessageConsumer;
import de.benjaminborbe.message.api.MessageConsumer;
import de.benjaminborbe.tools.guice.Modules;
import de.benjaminborbe.tools.osgi.BaseBundleActivator;
import de.benjaminborbe.tools.osgi.ServiceInfo;

public class KioskActivator extends BaseBundleActivator {

	@Inject
	private KioskService kioskService;

	@Inject
	private KioskBookingMessageConsumer kioskBookingMessageConsumer;

	@Override
	protected Modules getModules(final BundleContext context) {
		return new KioskModules(context);
	}

	@Override
	public Collection<ServiceInfo> getServiceInfos() {
		final Set<ServiceInfo> result = new HashSet<ServiceInfo>(super.getServiceInfos());
		result.add(new ServiceInfo(KioskService.class, kioskService));
		result.add(new ServiceInfo(MessageConsumer.class, kioskBookingMessageConsumer));
		return result;
	}

	@Override
	public Collection<ServiceTracker> getServiceTrackers(final BundleContext context) {
		final Set<ServiceTracker> serviceTrackers = new HashSet<ServiceTracker>(super.getServiceTrackers(context));
		// serviceTrackers.add(new KioskServiceTracker(kioskRegistry, context,
		// KioskService.class));
		return serviceTrackers;
	}
}
