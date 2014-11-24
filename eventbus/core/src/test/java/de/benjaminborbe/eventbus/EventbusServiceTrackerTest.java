package de.benjaminborbe.eventbus;

import de.benjaminborbe.eventbus.api.EventbusInitializedEventHandler;
import de.benjaminborbe.eventbus.api.EventbusService;
import de.benjaminborbe.lib.test.mock.EasyMockHelper;
import org.junit.Before;
import org.junit.Test;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;

public class EventbusServiceTrackerTest {

	private EasyMockHelper easyMockHelper;

	private Logger logger;

	private BundleContext bundleContext;

	@Before
	public void setUp() throws Exception {
		easyMockHelper = new EasyMockHelper();
		logger = easyMockHelper.createNiceMock(Logger.class);
		bundleContext = easyMockHelper.createNiceMock(BundleContext.class);
	}

	@Test
	public void testServiceRemoved() throws Exception {
		final EventbusService eventbusService = easyMockHelper.createMock(EventbusService.class);
		final EventbusInitializedEventHandler eventHandler = easyMockHelper.createNiceMock(EventbusInitializedEventHandler.class);
		eventbusService.removeHandler(null, eventHandler);
		easyMockHelper.replay();

		EventbusServiceTracker eventbusServiceTracker = new EventbusServiceTracker(logger, bundleContext, eventbusService);
		eventbusServiceTracker.serviceRemoved(eventHandler);

		easyMockHelper.verify();
	}

	@Test
	public void testServiceAdded() throws Exception {
		final EventbusService eventbusService = easyMockHelper.createMock(EventbusService.class);
		final EventbusInitializedEventHandler eventHandler = easyMockHelper.createNiceMock(EventbusInitializedEventHandler.class);
		eventbusService.addHandler(null, eventHandler);
		easyMockHelper.replay();

		EventbusServiceTracker eventbusServiceTracker = new EventbusServiceTracker(logger, bundleContext, eventbusService);
		eventbusServiceTracker.serviceAdded(eventHandler);

		easyMockHelper.verify();
	}
}
