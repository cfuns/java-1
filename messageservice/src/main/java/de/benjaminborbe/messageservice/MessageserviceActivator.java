package de.benjaminborbe.messageservice;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.osgi.framework.BundleContext;
import org.osgi.util.tracker.ServiceTracker;

import com.google.inject.Inject;

import de.benjaminborbe.cron.api.CronJob;
import de.benjaminborbe.messageservice.api.MessageConsumer;
import de.benjaminborbe.messageservice.api.MessageService;
import de.benjaminborbe.messageservice.guice.MessageserviceModules;
import de.benjaminborbe.messageservice.service.MessageConsumerCronJob;
import de.benjaminborbe.messageservice.service.MessageConsumerTracker;
import de.benjaminborbe.messageservice.util.MessageConsumerRegistry;
import de.benjaminborbe.tools.guice.Modules;
import de.benjaminborbe.tools.osgi.BaseBundleActivator;
import de.benjaminborbe.tools.osgi.ServiceInfo;

public class MessageserviceActivator extends BaseBundleActivator {

	@Inject
	private MessageService messageserviceService;

	@Inject
	private MessageConsumerRegistry messageConsumerRegistry;

	@Inject
	private MessageConsumerCronJob messageConsumerCronJob;

	@Override
	protected Modules getModules(final BundleContext context) {
		return new MessageserviceModules(context);
	}

	@Override
	public Collection<ServiceInfo> getServiceInfos() {
		final Set<ServiceInfo> result = new HashSet<ServiceInfo>(super.getServiceInfos());
		result.add(new ServiceInfo(MessageService.class, messageserviceService));
		result.add(new ServiceInfo(CronJob.class, messageConsumerCronJob, messageConsumerCronJob.getClass().getName()));
		return result;
	}

	@Override
	public Collection<ServiceTracker> getServiceTrackers(final BundleContext context) {
		final Set<ServiceTracker> serviceTrackers = new HashSet<ServiceTracker>(super.getServiceTrackers(context));
		serviceTrackers.add(new MessageConsumerTracker(logger, messageConsumerRegistry, context, MessageConsumer.class));
		return serviceTrackers;
	}
}
