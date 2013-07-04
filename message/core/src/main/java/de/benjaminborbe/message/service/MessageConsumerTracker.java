package de.benjaminborbe.message.service;

import de.benjaminborbe.message.api.MessageConsumer;
import de.benjaminborbe.message.util.MessageConsumerRegistry;
import de.benjaminborbe.tools.osgi.service.RegistryServiceTracker;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;

import javax.inject.Singleton;

@Singleton
public class MessageConsumerTracker extends RegistryServiceTracker<MessageConsumer> {

	private final Logger logger;

	public MessageConsumerTracker(final Logger logger, final MessageConsumerRegistry registry, final BundleContext context, final Class<?> clazz) {
		super(registry, context, clazz);
		this.logger = logger;
	}

	@Override
	protected void serviceRemoved(final MessageConsumer cronJob) {
		logger.trace("CronActivator.serviceRemoved() - MessageConsumer removed " + cronJob.getClass().getName());
		super.serviceRemoved(cronJob);
	}

	@Override
	protected void serviceAdded(final MessageConsumer cronJob) {
		logger.trace("CronActivator.serviceAdded() - MessageConsumer added " + cronJob.getClass().getName());
		super.serviceAdded(cronJob);
	}

}
