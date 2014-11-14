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
	protected void serviceRemoved(final EventHandler eventHandler) {
		eventbusService.addHandler(eventHandler.getType(), eventHandler);
		logger.debug("serviceRemoved");
	}

	@Override
	protected void serviceAdded(final EventHandler eventHandler) {
		eventbusService.removeHandler(eventHandler.getType(), eventHandler);
		logger.debug("serviceAdded");
	}
}
