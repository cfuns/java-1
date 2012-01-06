package de.benjaminborbe.eventbus.test;

import java.util.ArrayList;
import java.util.List;

import org.apache.felix.http.api.ExtHttpService;
import org.apache.felix.ipojo.junit4osgi.OSGiTestCase;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

import de.benjaminborbe.eventbus.api.EventBusInitializedEvent;
import de.benjaminborbe.eventbus.api.EventBusInitializedEventHandler;
import de.benjaminborbe.eventbus.api.EventBusService;
import de.benjaminborbe.tools.osgi.mock.ExtHttpServiceMock;

public class EventbusTest extends OSGiTestCase {

	@Override
	protected void setUp() throws Exception {
		super.setUp();
	}

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testGetExtHttpService() {

		final BundleContext bundleContext = getContext();
		assertNotNull(bundleContext);
		final ExtHttpServiceMock extHttpService = new ExtHttpServiceMock();
		assertNotNull(extHttpService);
		// zum start: keine Dienste registriert
		assertEquals(0, extHttpService.getRegisterFilterCallCounter());
		assertEquals(0, extHttpService.getRegisterServletCallCounter());
		assertEquals(0, extHttpService.getUnregisterFilterCallCounter());
		assertEquals(0, extHttpService.getUnregisterServletCallCounter());
		final ServiceRegistration serviceRegistration = bundleContext.registerService(ExtHttpService.class.getName(), extHttpService, null);
		assertNotNull(serviceRegistration);
		// nach start: Dienste vorhanden?
		assertTrue("no filters registered", extHttpService.getRegisterFilterCallCounter() > 0);
		assertTrue("no servlets registered.", extHttpService.getRegisterServletCallCounter() > 0);
		assertEquals(0, extHttpService.getUnregisterFilterCallCounter());
		assertEquals(0, extHttpService.getUnregisterServletCallCounter());

		// do unregister
		serviceRegistration.unregister();

		assertTrue("no servlets unregistered", extHttpService.getUnregisterServletCallCounter() > 0);
		assertEquals(extHttpService.getRegisterServletCallCounter(), extHttpService.getRegisterServletCallCounter());
		assertEquals(extHttpService.getRegisterFilterCallCounter(), extHttpService.getUnregisterFilterCallCounter());

	}

	public void testGetService() {
		final EventBusService eventBusService = (EventBusService) getServiceObject(EventBusService.class.getName(), null);
		assertNotNull(eventBusService);
		assertEquals("de.benjaminborbe.eventbus.service.EventBusServiceImpl", eventBusService.getClass().getName());
	}

	public void testEventBus() {
		final EventBusService eventBus = (EventBusService) getServiceObject(EventBusService.class.getName(), null);
		assertNotNull(eventBus);
		assertEquals("de.benjaminborbe.eventbus.service.EventBusServiceImpl", eventBus.getClass().getName());

		final List<EventBusInitializedEvent> events = new ArrayList<EventBusInitializedEvent>();

		assertEquals(0, eventBus.getHandlerCount(EventBusInitializedEvent.TYPE));
		assertFalse(eventBus.isEventHandled(EventBusInitializedEvent.TYPE));
		eventBus.addHandler(EventBusInitializedEvent.TYPE, new EventBusInitializedEventHandler() {

			@Override
			public void onInitialize(final EventBusInitializedEvent event) {
				events.add(event);
			}
		});
		assertEquals(1, eventBus.getHandlerCount(EventBusInitializedEvent.TYPE));
		assertTrue(eventBus.isEventHandled(EventBusInitializedEvent.TYPE));
		assertEquals(0, events.size());
		eventBus.fireEvent(new EventBusInitializedEvent());
		assertEquals(1, events.size());
	}
}
