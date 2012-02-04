package de.benjaminborbe.authentication.test;

import org.apache.felix.http.api.ExtHttpService;
import org.apache.felix.ipojo.junit4osgi.OSGiTestCase;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

import de.benjaminborbe.authentication.api.AuthenticationService;
import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.tools.osgi.mock.ExtHttpServiceMock;

public class AuthenticationTest extends OSGiTestCase {

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

	public void testGetAuthenticationService() {
		final Object serviceObject = getServiceObject(AuthenticationService.class.getName(), null);
		final AuthenticationService service = (AuthenticationService) serviceObject;
		assertNotNull(service);
		assertEquals("de.benjaminborbe.authentication.service.AuthenticationServiceImpl", service.getClass().getName());
	}

	public void testRegister() {
		final Object serviceObject = getServiceObject(AuthenticationService.class.getName(), null);
		final AuthenticationService service = (AuthenticationService) serviceObject;
		assertNotNull(service);
		assertEquals("de.benjaminborbe.authentication.service.AuthenticationServiceImpl", service.getClass().getName());

		final String sessionId = "asdf";
		final String username = "testuser";
		final String password = "testpassword";

		final SessionIdentifier sessionIdentifier = new SessionIdentifier(sessionId);

		assertTrue(service.register(sessionIdentifier, username, password));
		assertFalse("must fail, because already registered", service.register(sessionIdentifier, username, password));
	}

	public void testVerifyCredentials() {
		final Object serviceObject = getServiceObject(AuthenticationService.class.getName(), null);
		final AuthenticationService service = (AuthenticationService) serviceObject;
		assertNotNull(service);
		assertEquals("de.benjaminborbe.authentication.service.AuthenticationServiceImpl", service.getClass().getName());

		final String sessionId = "asdf";
		final String username = "testuser";
		final String password = "testpassword";

		final SessionIdentifier sessionIdentifier = new SessionIdentifier(sessionId);

		service.register(sessionIdentifier, username, password);
		assertTrue(service.verifyCredential(username, password));
		assertFalse(service.verifyCredential("wrong", password));
		assertFalse(service.verifyCredential(username, "wrong"));
	}
}
