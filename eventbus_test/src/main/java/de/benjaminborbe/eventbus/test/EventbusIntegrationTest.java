package de.benjaminborbe.eventbus.test;

import java.util.ArrayList;
import java.util.List;

import org.apache.felix.http.api.ExtHttpService;
import org.apache.felix.ipojo.junit4osgi.OSGiTestCase;
import org.junit.Test;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

import de.benjaminborbe.eventbus.api.aEventbusInitializedEvent;
import de.benjaminborbe.eventbus.api.aEventbusInitializedEventHandler;
import de.benjaminborbe.eventbus.api.aEventbusService;
import de.benjaminborbe.tools.osgi.mock.ExtHttpServiceMock;
import de.benjaminborbe.tools.url.UrlUtilImpl;

public class EventbusIntegrationTest extends OSGiTestCase {

	@Override
	protected void setUp() throws Exception {
		super.setUp();
	}

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
	}

	@Test
	public void testGetExtHttpService() {

		final BundleContext bundleContext = getContext();
		assertNotNull(bundleContext);
		final ExtHttpServiceMock extHttpService = new ExtHttpServiceMock(new UrlUtilImpl());
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

	@Test
	public void testGetService() {
		final aEventbusService EventbusService = (aEventbusService) getServiceObject(aEventbusService.class.getName(), null);
		assertNotNull(EventbusService);
		assertEquals("de.benjaminborbe.eventbus.service.EventbusServiceImpl", EventbusService.getClass().getName());
	}

	@Test
	public void testEventbus() {
		final aEventbusService Eventbus = (aEventbusService) getServiceObject(aEventbusService.class.getName(), null);
		assertNotNull(Eventbus);
		assertEquals("de.benjaminborbe.eventbus.service.EventbusServiceImpl", Eventbus.getClass().getName());

		final List<aEventbusInitializedEvent> events = new ArrayList<aEventbusInitializedEvent>();

		assertEquals(0, Eventbus.getHandlerCount(aEventbusInitializedEvent.TYPE));
		assertFalse(Eventbus.isEventHandled(aEventbusInitializedEvent.TYPE));
		Eventbus.addHandler(aEventbusInitializedEvent.TYPE, new aEventbusInitializedEventHandler() {

			@Override
			public void onInitialize(final aEventbusInitializedEvent event) {
				events.add(event);
			}
		});
		assertEquals(1, Eventbus.getHandlerCount(aEventbusInitializedEvent.TYPE));
		assertTrue(Eventbus.isEventHandled(aEventbusInitializedEvent.TYPE));
		assertEquals(0, events.size());
		Eventbus.fireEvent(new aEventbusInitializedEvent());
		assertEquals(1, events.size());
	}
}
