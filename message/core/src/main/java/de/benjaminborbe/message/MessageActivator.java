package de.benjaminborbe.message;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.osgi.framework.BundleContext;
import org.osgi.util.tracker.ServiceTracker;

import com.google.inject.Inject;

import de.benjaminborbe.configuration.api.ConfigurationDescription;
import de.benjaminborbe.cron.api.CronJob;
import de.benjaminborbe.message.api.MessageConsumer;
import de.benjaminborbe.message.api.MessageService;
import de.benjaminborbe.message.config.MessageConfig;
import de.benjaminborbe.message.guice.MessageModules;
import de.benjaminborbe.message.service.MessageConsumerCronJob;
import de.benjaminborbe.message.service.MessageConsumerTracker;
import de.benjaminborbe.message.service.MessageUnlockCronJob;
import de.benjaminborbe.message.util.MessageConsumerRegistry;
import de.benjaminborbe.tools.guice.Modules;
import de.benjaminborbe.tools.osgi.BaseBundleActivator;
import de.benjaminborbe.tools.osgi.ServiceInfo;

public class MessageActivator extends BaseBundleActivator {

	@Inject
	private MessageConfig messageConfig;

	@Inject
	private MessageService messageService;

	@Inject
	private MessageConsumerRegistry messageConsumerRegistry;

	@Inject
	private MessageConsumerCronJob messageConsumerCronJob;

	@Inject
	private MessageUnlockCronJob messageUnlockCronJob;

	@Override
	protected Modules getModules(final BundleContext context) {
		return new MessageModules(context);
	}

	@Override
	public Collection<ServiceInfo> getServiceInfos() {
		final Set<ServiceInfo> result = new HashSet<ServiceInfo>(super.getServiceInfos());
		result.add(new ServiceInfo(MessageService.class, messageService));
		result.add(new ServiceInfo(CronJob.class, messageConsumerCronJob, messageConsumerCronJob.getClass().getName()));
		result.add(new ServiceInfo(CronJob.class, messageUnlockCronJob, messageUnlockCronJob.getClass().getName()));
		for (final ConfigurationDescription configuration : messageConfig.getConfigurations()) {
			result.add(new ServiceInfo(ConfigurationDescription.class, configuration, configuration.getName()));
		}
		return result;
	}

	@Override
	public Collection<ServiceTracker> getServiceTrackers(final BundleContext context) {
		final Set<ServiceTracker> serviceTrackers = new HashSet<ServiceTracker>(super.getServiceTrackers(context));
		serviceTrackers.add(new MessageConsumerTracker(logger, messageConsumerRegistry, context, MessageConsumer.class));
		return serviceTrackers;
	}
}
