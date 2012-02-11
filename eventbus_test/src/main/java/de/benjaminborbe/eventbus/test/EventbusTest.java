package de.benjaminborbe.eventbus.test;

import java.util.ArrayList;
import java.util.List;

import org.apache.felix.http.api.ExtHttpService;
import org.apache.felix.ipojo.junit4osgi.OSGiTestCase;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

import de.benjaminborbe.eventbus.api.EventbusInitializedEvent;
import de.benjaminborbe.eventbus.api.EventbusInitializedEventHandler;
import de.benjaminborbe.eventbus.api.EventbusService;
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
		final EventbusService EventbusService = (EventbusService) getServiceObject(EventbusService.class.getName(), null);
		assertNotNull(EventbusService);
		assertEquals("de.benjaminborbe.eventbus.service.EventbusServiceImpl", EventbusService.getClass().getName());
	}

	public void testEventbus() {
		final EventbusService Eventbus = (EventbusService) getServiceObject(EventbusService.class.getName(), null);
		assertNotNull(Eventbus);
		assertEquals("de.benjaminborbe.eventbus.service.EventbusServiceImpl", Eventbus.getClass().getName());

		final List<EventbusInitializedEvent> events = new ArrayList<EventbusInitializedEvent>();

		assertEquals(0, Eventbus.getHandlerCount(EventbusInitializedEvent.TYPE));
		assertFalse(Eventbus.isEventHandled(EventbusInitializedEvent.TYPE));
		Eventbus.addHandler(EventbusInitializedEvent.TYPE, new EventbusInitializedEventHandler() {

			@Override
			public void onInitialize(final EventbusInitializedEvent event) {
				events.add(event);
			}
		});
		assertEquals(1, Eventbus.getHandlerCount(EventbusInitializedEvent.TYPE));
		assertTrue(Eventbus.isEventHandled(EventbusInitializedEvent.TYPE));
		assertEquals(0, events.size());
		Eventbus.fireEvent(new EventbusInitializedEvent());
		assertEquals(1, events.size());
	}
}
