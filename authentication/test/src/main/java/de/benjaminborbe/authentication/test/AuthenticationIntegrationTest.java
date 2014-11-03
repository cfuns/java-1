package de.benjaminborbe.authentication.test;

import de.benjaminborbe.api.ValidationException;
import de.benjaminborbe.authentication.api.AuthenticationService;
import de.benjaminborbe.authentication.api.AuthenticationServiceException;
import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.authentication.api.UserIdentifier;
import de.benjaminborbe.storage.api.StorageService;
import de.benjaminborbe.test.osgi.TestCaseOsgi;
import de.benjaminborbe.test.osgi.TestUtil;
import de.benjaminborbe.tools.osgi.mock.ExtHttpServiceMock;
import de.benjaminborbe.tools.url.UrlUtilImpl;
import org.apache.felix.http.api.ExtHttpService;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

public class AuthenticationIntegrationTest extends TestCaseOsgi {

	public static final String SYSTEM_USERNAME = "cron";

	private final String validateEmailBaseUrl = "http://example.com/test";

	private final String shortenUrl = "http://bb/bb/s";

	@Override
	protected void setUp() throws Exception {
		super.setUp();

		final AuthenticationService authenticationService = getService(AuthenticationService.class);
		final StorageService storageService = getService(StorageService.class);
		final TestUtil testUtil = new TestUtil(authenticationService, storageService);
		testUtil.clearUserColumnFamily();
	}

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
	}

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

	public void testGetAuthenticationService() {
		final Object serviceObject = getServiceObject(AuthenticationService.class.getName(), null);
		final AuthenticationService service = (AuthenticationService) serviceObject;
		assertNotNull(service);
		assertEquals("de.benjaminborbe.authentication.core.service.AuthenticationServiceImpl", service.getClass().getName());
	}

	public void testRegister() throws Exception {
		final Object serviceObject = getServiceObject(AuthenticationService.class.getName(), null);
		final AuthenticationService service = (AuthenticationService) serviceObject;
		assertNotNull(service);
		assertEquals("de.benjaminborbe.authentication.core.service.AuthenticationServiceImpl", service.getClass().getName());

		final SessionIdentifier sessionIdentifier = new SessionIdentifier(TestUtil.SESSION_ID);
		final UserIdentifier userIdentifier = new UserIdentifier(TestUtil.LOGIN_USER);

		// if user already exists unregister first
		if (service.verifyCredential(userIdentifier, TestUtil.PASSWORD)) {
			service.login(sessionIdentifier, userIdentifier, TestUtil.PASSWORD);
			service.unregister(sessionIdentifier);
		}

		assertFalse(service.isLoggedIn(sessionIdentifier));
		assertFalse(service.verifyCredential(userIdentifier, TestUtil.PASSWORD));
		service.register(sessionIdentifier, shortenUrl, validateEmailBaseUrl, TestUtil.LOGIN_USER, TestUtil.EMAIL, TestUtil.PASSWORD);
		try {
			service.register(sessionIdentifier, shortenUrl, validateEmailBaseUrl, TestUtil.LOGIN_USER, TestUtil.EMAIL, TestUtil.PASSWORD);
			fail("must fail, because already registered");
		} catch (final ValidationException e) {
			assertNotNull(e);
		}
	}

	public void testVerifyCredentials() throws Exception {
		final Object serviceObject = getServiceObject(AuthenticationService.class.getName(), null);
		final AuthenticationService service = (AuthenticationService) serviceObject;
		assertNotNull(service);
		assertEquals("de.benjaminborbe.authentication.core.service.AuthenticationServiceImpl", service.getClass().getName());

		final SessionIdentifier sessionIdentifier = new SessionIdentifier(TestUtil.SESSION_ID);
		final UserIdentifier userIdentifier = new UserIdentifier(TestUtil.LOGIN_USER);

		// if user already exists unregister first
		if (service.verifyCredential(userIdentifier, TestUtil.PASSWORD)) {
			service.login(sessionIdentifier, userIdentifier, TestUtil.PASSWORD);
			service.unregister(sessionIdentifier);
		}

		service.logout(sessionIdentifier);

		assertFalse(service.isLoggedIn(sessionIdentifier));
		service.register(sessionIdentifier, shortenUrl, validateEmailBaseUrl, TestUtil.LOGIN_USER, TestUtil.EMAIL, TestUtil.PASSWORD);
		assertTrue(service.verifyCredential(new UserIdentifier(TestUtil.LOGIN_USER), TestUtil.PASSWORD));
		assertFalse(service.verifyCredential(new UserIdentifier("wrong"), TestUtil.PASSWORD));
		assertFalse(service.verifyCredential(new UserIdentifier(TestUtil.LOGIN_USER), "wrong"));
	}

	public void testCreateUser() throws AuthenticationServiceException, ValidationException {
		final AuthenticationService authenticationService = getService(AuthenticationService.class);
		final StorageService storageService = getService(StorageService.class);
		final TestUtil testUtil = new TestUtil(authenticationService, storageService);
		final SessionIdentifier sessionIdentifier = new SessionIdentifier(TestUtil.SESSION_ID);
		assertNotNull(testUtil.createUser(sessionIdentifier));
		assertFalse(authenticationService.isSuperAdmin(sessionIdentifier));
	}

	public void testCreateSuperAdmin() throws Exception {
		final AuthenticationService authenticationService = getService(AuthenticationService.class);
		final StorageService storageService = getService(StorageService.class);
		final TestUtil testUtil = new TestUtil(authenticationService, storageService);
		final SessionIdentifier sessionIdentifier = new SessionIdentifier(TestUtil.SESSION_ID);
		assertNotNull(testUtil.createSuperAdmin(sessionIdentifier));
		assertTrue(authenticationService.isSuperAdmin(sessionIdentifier));
	}

	public void testS() throws Exception {
		final AuthenticationService authenticationService = getService(AuthenticationService.class);
		assertFalse(authenticationService.existsUser(new UserIdentifier(SYSTEM_USERNAME)));
		final SessionIdentifier sessionIdentifier = authenticationService.createSystemUser(SYSTEM_USERNAME);
		assertTrue(authenticationService.existsUser(new UserIdentifier(SYSTEM_USERNAME)));
	}

}
