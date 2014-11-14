package de.benjaminborbe.eventbus;

import de.benjaminborbe.eventbus.api.EventHandler;
import de.benjaminborbe.tools.osgi.service.BaseServiceTracker;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;

public class EventbusServiceTracker extends BaseServiceTracker<EventHandler> {

	private final Logger logger;

	public EventbusServiceTracker(final Logger logger, final BundleContext context) {
		super(context, EventHandler.class);
		this.logger = logger;
	}

	@Override
	protected void serviceRemoved(final EventHandler service) {
		logger.debug("serviceRemoved");
	}

	@Override
	protected void serviceAdded(final EventHandler service) {
		logger.debug("serviceAdded");
	}
}
