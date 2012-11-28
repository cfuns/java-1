package de.benjaminborbe.messageservice;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.osgi.framework.BundleContext;
import org.osgi.util.tracker.ServiceTracker;

import com.google.inject.Inject;

import de.benjaminborbe.messageservice.api.MessageserviceService;
import de.benjaminborbe.messageservice.guice.MessageserviceModules;
import de.benjaminborbe.tools.guice.Modules;
import de.benjaminborbe.tools.osgi.BaseBundleActivator;
import de.benjaminborbe.tools.osgi.ServiceInfo;

public class MessageserviceActivator extends BaseBundleActivator {

	@Inject
	private MessageserviceService messageserviceService;

	@Override
	protected Modules getModules(final BundleContext context) {
		return new MessageserviceModules(context);
	}

	@Override
	public Collection<ServiceInfo> getServiceInfos() {
		final Set<ServiceInfo> result = new HashSet<ServiceInfo>(super.getServiceInfos());
		result.add(new ServiceInfo(MessageserviceService.class, messageserviceService));
		return result;
	}

	@Override
	public Collection<ServiceTracker> getServiceTrackers(final BundleContext context) {
		final Set<ServiceTracker> serviceTrackers = new HashSet<ServiceTracker>(super.getServiceTrackers(context));
		// serviceTrackers.add(new MessageserviceServiceTracker(messageserviceRegistry,
		// context,
		// MessageserviceService.class));
		return serviceTrackers;
	}
}
