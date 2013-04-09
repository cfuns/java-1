package de.benjaminborbe.note.test;

import de.benjaminborbe.authentication.api.AuthenticationService;
import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.note.api.NoteDto;
import de.benjaminborbe.note.api.NoteService;
import de.benjaminborbe.storage.api.StorageService;
import de.benjaminborbe.test.osgi.TestCaseOsgi;
import de.benjaminborbe.test.osgi.TestUtil;
import de.benjaminborbe.tools.osgi.mock.ExtHttpServiceMock;
import de.benjaminborbe.tools.url.UrlUtilImpl;
import org.apache.felix.http.api.ExtHttpService;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

public class NoteIntegrationTest extends TestCaseOsgi {

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

	// @Test public void testServices() throws Exception {
	// final BundleContext bundleContext = getContext();
	// assertNotNull(bundleContext);
	// for (final ServiceReference a : bundleContext.getAllServiceReferences(null, null)) {
	// // final Bundle bundle = a.getBundle();
	// final Object service = bundleContext.getService(a);
	// System.err.println(service);
	// }
	// }

	public void testNoteService() {
		final Object serviceObject = getServiceObject(NoteService.class.getName(), null);
		final NoteService service = (NoteService) serviceObject;
		assertNotNull(service);
		assertEquals("de.benjaminborbe.note.service.NoteServiceImpl", service.getClass().getName());
	}

	public void testCrud() throws Exception {
		final AuthenticationService authenticationService = getService(AuthenticationService.class);
		final StorageService storageService = getService(StorageService.class);
		final TestUtil testUtil = new TestUtil(authenticationService, storageService);
		final NoteService noteService = getService(NoteService.class);
		final SessionIdentifier sessionIdentifier = testUtil.createSessionIdentifier();
		testUtil.createUser(sessionIdentifier);

		final NoteDto noteDto = new NoteDto();
		final String title = "test";
		final String content = "";
		noteDto.setTitle(title);
		noteDto.setContent(content);
		noteService.createNote(sessionIdentifier, noteDto);
	}

}
