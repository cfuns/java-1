package de.benjaminborbe.distributed.search.test;

import java.net.URL;

import org.apache.felix.http.api.ExtHttpService;
import org.apache.felix.ipojo.junit4osgi.OSGiTestCase;
import org.junit.Test;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

import de.benjaminborbe.distributed.search.api.DistributedSearchService;
import de.benjaminborbe.tools.osgi.mock.ExtHttpServiceMock;
import de.benjaminborbe.tools.url.UrlUtilImpl;

public class DistributedSearchIntegrationTest extends OSGiTestCase {

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

	// @Test public void testServices() throws Exception {
	// final BundleContext bundleContext = getContext();
	// assertNotNull(bundleContext);
	// for (final ServiceReference a : bundleContext.getAllServiceReferences(null, null)) {
	// // final Bundle bundle = a.getBundle();
	// final Object service = bundleContext.getService(a);
	// System.err.println(service);
	// }
	// }

	@Test
	public void testDistributedSearchService() {
		final Object serviceObject = getServiceObject(DistributedSearchService.class.getName(), null);
		final DistributedSearchService service = (DistributedSearchService) serviceObject;
		assertNotNull(service);
		assertEquals("de.benjaminborbe.distributed.search.service.DistributedSearchServiceImpl", service.getClass().getName());
	}

	@Test
	public void testSearch() throws Exception {
		final Object serviceObject = getServiceObject(DistributedSearchService.class.getName(), null);
		final DistributedSearchService service = (DistributedSearchService) serviceObject;

		final String index = "defaultIndex";
		final URL url = new URL("http://www.example.com/a");
		final String title = "title";
		final String content = "content";

		assertTrue(service.search(index, "content", 1).isEmpty());

		service.addToIndex(index, url, title, content);

		assertFalse(service.search(index, "content", 1).isEmpty());

		service.clear(index);

		assertTrue(service.search(index, "content", 1).isEmpty());

		assertFalse(true);
	}
}
