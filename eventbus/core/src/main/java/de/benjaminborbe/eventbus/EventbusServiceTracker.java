package de.benjaminborbe.eventbus;

import de.benjaminborbe.eventbus.api.EventHandler;
import de.benjaminborbe.eventbus.api.EventbusService;
import de.benjaminborbe.tools.osgi.service.BaseServiceTracker;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;

public class EventbusServiceTracker extends BaseServiceTracker<EventHandler> {

	private final Logger logger;

	private final EventbusService eventbusService;

	public EventbusServiceTracker(final Logger logger, final BundleContext context, final EventbusService eventbusService) {
		super(context, EventHandler.class);
		this.logger = logger;
		this.eventbusService = eventbusService;
	}

	@Override
	protected void serviceAdded(final EventHandler eventHandler) {
		logger.debug("serviceAdded: {}", eventHandler.getType());
		eventbusService.addHandler(eventHandler.getType(), eventHandler);
	}

	@Override
	protected void serviceRemoved(final EventHandler eventHandler) {
		logger.debug("serviceRemoved: {}", eventHandler.getType());
		eventbusService.removeHandler(eventHandler.getType(), eventHandler);
	}
}
