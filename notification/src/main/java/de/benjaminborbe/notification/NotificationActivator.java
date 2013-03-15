package de.benjaminborbe.notification;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.osgi.framework.BundleContext;
import org.osgi.util.tracker.ServiceTracker;

import com.google.inject.Inject;

import de.benjaminborbe.notification.api.NotificationService;
import de.benjaminborbe.notification.guice.NotificationModules;
import de.benjaminborbe.tools.guice.Modules;
import de.benjaminborbe.tools.osgi.BaseBundleActivator;
import de.benjaminborbe.tools.osgi.ServiceInfo;

public class NotificationActivator extends BaseBundleActivator {

	@Inject
	private NotificationService notificationService;

	@Override
	protected Modules getModules(final BundleContext context) {
		return new NotificationModules(context);
	}

	@Override
	public Collection<ServiceInfo> getServiceInfos() {
		final Set<ServiceInfo> result = new HashSet<ServiceInfo>(super.getServiceInfos());
		result.add(new ServiceInfo(NotificationService.class, notificationService));
		return result;
	}

	@Override
	public Collection<ServiceTracker> getServiceTrackers(final BundleContext context) {
		final Set<ServiceTracker> serviceTrackers = new HashSet<ServiceTracker>(super.getServiceTrackers(context));
		// serviceTrackers.add(new NotificationServiceTracker(notificationRegistry, context,
		// NotificationService.class));
		return serviceTrackers;
	}
}
